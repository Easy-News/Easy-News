package shimp.easy_news.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shimp.easy_news.user.domain.UserEmailSchedule;

import java.time.LocalTime;

public interface UserEmailScheduleRepository extends JpaRepository<UserEmailSchedule, Long> {

    UserEmailSchedule findByScheduledTimeAndSentTodayFalse(LocalTime scheduledAt);
}
