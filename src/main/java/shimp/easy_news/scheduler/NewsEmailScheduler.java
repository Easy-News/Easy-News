package shimp.easy_news.scheduler;

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

    @Scheduled(cron = "0 * * * * *")
    public void sendScheduledNewsEmails() {

//        LocalTime now = LocalTime.of(3, 40, 0);
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        Optional<User> user = userRepository.findByMailingTimeAndSentTodayFalse(now);

//        Category category = Category.IT_SCIENCE;

        User u = user.orElse(null);
        if (u == null) {
            log.info("메일링 대상 사용자가 없습니다.");
            return;
        }
        Category category = u.getInterested();

        // 1. 뉴스 가져오기 (설명, 요약)
        NewsDescriptionResDto newsDescriptionResDto = newsService.buildDescriptionDataByCategory(category);
        NewsSummaryResDto newsSummaryResDto = newsService.buildSummaryData();

        // 2. GPT에 한 번에 전달
        String explanation = gptClientService.callDescriptionGpt(
                newsDescriptionResDto.getCombinedNewsText(),
                category,
                GptRole.EXPLANATION
        );
        String summary = gptClientService.callSummaryGpt(
                newsSummaryResDto.getCombinedNewsText(),
                category,
                GptRole.SUMMARY);

        // 3. 이메일 전송
        String fullMailBody = summary + "\n\n" + explanation + "\n\n" + newsDescriptionResDto.getSourceListText();
        try {
            // 1. Go 서버로 보낼 데이터를 준비합니다.
            String recipientEmail = user.get().getEmail();
            String emailSubject = "[Easy News] " + user.get().getNickname() + "님, 오늘의 뉴스 알려드립니다.";
            String emailBody = fullMailBody; // 이 변수는 이미 값이 할당되어 있다고 가정합니다.

            // 2. 전송할 데이터를 JSON 형식의 문자열로 만듭니다.
            // 본문에 포함될 수 있는 따옴표나 개행문자 등 특수문자가 JSON 형식을 깨뜨리지 않도록 처리합니다.
            // 참고: 보다 안정적인 운영 환경에서는 'Jackson'이나 'Gson' 라이브러리 사용을 권장합니다.
            String escapedBody = emailBody.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\r", "\\r")
                    .replace("\n", "\\n")
                    .replace("\t", "\\t");

            String jsonPayload = String.format(
                    "{\"email\":\"%s\", \"subject\":\"%s\", \"body\":\"%s\"}",
                    recipientEmail, emailSubject, escapedBody
            );

            // 3. Java 내장 HTTP 클라이언트를 사용하여 Go 서버에 요청을 보냅니다.
            // (Java 11 이상)
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create("http://email-module:5525/email"))
                    .header("Content-Type", "application/json")
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // 4. 요청을 보내고 응답을 받습니다.
            java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

            // 5. 결과를 콘솔에 출력하여 성공 여부를 확인합니다.
            if (response.statusCode() == 200) {
                System.out.println("Go 서버를 통한 이메일 요청 성공: " + response.body());
            } else {
                System.err.println("Go 서버 요청 실패 | 상태 코드: " + response.statusCode() + " | 응답: " + response.body());
            }

        } catch (java.io.IOException | InterruptedException e) {
            System.err.println("Go 서버로 이메일 요청을 보내는 중 오류가 발생했습니다.");
            e.printStackTrace();
            // 필요 시, 이곳에 예외 상황을 처리하는 로직을 추가할 수 있습니다.
        }


        // 4. 전송 처리
        user.get().markAsSentToday();
    }
}

