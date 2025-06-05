package shimp.easy_news.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import shimp.easy_news.gpt.constant.GptRole;
import shimp.easy_news.gpt.service.GptClientService;
import shimp.easy_news.news.constant.SubCategory;
import shimp.easy_news.news.dto.NewsDescriptionResDto;
import shimp.easy_news.news.service.NewsEmailService;
import shimp.easy_news.news.service.NewsService;
import shimp.easy_news.user.domain.User;
import shimp.easy_news.user.domain.UserEmailSchedule;
import shimp.easy_news.user.repository.UserEmailScheduleRepository;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class NewsEmailScheduler {

    private final UserEmailScheduleRepository userEmailScheduleRepository;
    private final NewsService newsService;
    private final GptClientService gptClientService;
    private final NewsEmailService emailService;

    @Scheduled(cron = "0 * * * * *")
    public void sendScheduledNewsEmails() {

//        LocalTime now = LocalTime.parse("03:40:00");
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        UserEmailSchedule schedule = userEmailScheduleRepository.findByScheduledTimeAndSentTodayFalse(now);

        User user = schedule.getUser();
        SubCategory subCategory = schedule.getSubCategory();

        // 1. 뉴스 가져오기 (설명)
        // TODO : 여기서 뉴스 요약에 필요한 데이터 가져오면 됨
        NewsDescriptionResDto newsDescriptionResDto = newsService.buildDescriptionDataBySubCategory(subCategory);

        // 2. GPT에 한 번에 전달
        String explanation = gptClientService.callDescriptionGpt(
                newsDescriptionResDto.getCombinedNewsText(),
                subCategory,
                GptRole.EXPLANATION
        );
        // TODO : 여기서 뉴스 요약 gpt 호출하면 됨

        // 3. 이메일 전송
        // TODO : 여기서 뉴스 요약 내용 이메일에 추가 (fullMailBody 하나로)
        String fullMailBody = explanation + "\n\n" + newsDescriptionResDto.getSourceListText();
        emailService.sendNewsEmail(
                user.getEmail(),
                "[Easy News] " + user.getNickname()+"님, 오늘의 뉴스 알려드립니다.",
                fullMailBody
        );

        // 4. 전송 처리
        UserEmailSchedule.builder()
                .sentToday(true)
                .build();

        userEmailScheduleRepository.save(schedule);
    }
}

