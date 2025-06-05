package shimp.easy_news;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shimp.easy_news.scheduler.NewsEmailScheduler;

@SpringBootTest
public class NewsEmailSchedulerTest {

    @Autowired
    private NewsEmailScheduler scheduler;

    @Test
    void testScheduledEmailSend() {
        scheduler.sendScheduledNewsEmails();
    }
}

