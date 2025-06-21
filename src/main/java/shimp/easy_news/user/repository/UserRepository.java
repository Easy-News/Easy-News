package shimp.easy_news.user.repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shimp.easy_news.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    User findByMailingTimeAndSentTodayFalse(LocalTime mailingTime);
}
