package shimp.easy_news.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper; // Jackson ObjectMapper 임포트
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import shimp.easy_news.gpt.constant.GptRole;
import shimp.easy_news.gpt.service.GptClientService;
import shimp.easy_news.news.constant.Category;
import shimp.easy_news.news.dto.NewsDescriptionResDto;
import shimp.easy_news.news.dto.NewsSummaryResDto;
import shimp.easy_news.news.service.NewsEmailService;
import shimp.easy_news.news.service.NewsService;
import shimp.easy_news.user.domain.User;
import shimp.easy_news.user.repository.UserRepository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.util.Optional;


@Component
@RequiredArgsConstructor
@Slf4j
public class NewsEmailScheduler {

    private final NewsService newsService;
    private final GptClientService gptClientService;
    private final NewsEmailService emailService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper를 필드로 주입받거나 생성하여 재사용

    // JSON 요청 본문을 위한 내부 DTO 클래스
    @Getter
    @AllArgsConstructor
    private static class EmailRequestDto {
        private String email;
        private String subject;
        private String body;
    }

    @Scheduled(cron = "0 * * * * *")
    public void sendScheduledNewsEmails() {

        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        Optional<User> userOptional = userRepository.findByMailingTimeAndSentTodayFalse(now);

        if (userOptional.isEmpty()) {
            log.info("메일링 대상 사용자가 없습니다.");
            return;
        }
        User user = userOptional.get();
        Category category = user.getInterested();

        // 1. 뉴스 가져오기
        NewsDescriptionResDto newsDescriptionResDto = newsService.buildDescriptionDataByCategory(category);
        NewsSummaryResDto newsSummaryResDto = newsService.buildSummaryData();

        // 2. GPT 요약 및 설명 요청
        String explanation = gptClientService.callDescriptionGpt(newsDescriptionResDto.getCombinedNewsText(), category, GptRole.EXPLANATION);
        String summary = gptClientService.callSummaryGpt(newsSummaryResDto.getCombinedNewsText(), category, GptRole.SUMMARY);

        // 3. 이메일 전송
        String fullMailBody = summary + "\n\n" + explanation + "\n\n" + newsDescriptionResDto.getSourceListText();
        try {
            // 1. Go 서버로 보낼 데이터를 DTO 객체에 담습니다.
            String recipientEmail = user.getEmail();
            String emailSubject = "[Easy News] " + user.getNickname() + "님, 오늘의 뉴스 알려드립니다.";
            EmailRequestDto emailRequestDto = new EmailRequestDto(recipientEmail, emailSubject, fullMailBody);

            // 2. ObjectMapper를 사용하여 DTO 객체를 JSON 문자열로 변환합니다.
            // 이 과정에서 줄바꿈, 따옴표 등 모든 특수문자가 자동으로 처리됩니다.
            String jsonPayload = objectMapper.writeValueAsString(emailRequestDto);

            // 3. Java 내장 HTTP 클라이언트를 사용하여 Go 서버에 요청을 보냅니다.
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://email-module:5525/email"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // 4. 요청을 보내고 응답을 받습니다.
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 5. 결과를 콘솔에 출력하여 성공 여부를 확인합니다.
            if (response.statusCode() == 200) {
                log.info("Go 서버를 통한 이메일 요청 성공: " + response.body());
            } else {
                log.error("Go 서버 요청 실패 | 상태 코드: " + response.statusCode() + " | 응답: " + response.body());
            }

        } catch (IOException | InterruptedException e) {
            log.error("Go 서버로 이메일 요청을 보내는 중 오류가 발생했습니다.", e);
            // 필요 시, 이곳에 예외 상황을 처리하는 로직을 추가할 수 있습니다.
            Thread.currentThread().interrupt(); // InterruptedException 처리 시 권장
        }

        // 4. 전송 완료 처리
        user.markAsSentToday();
    }
}
