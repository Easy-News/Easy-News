package shimp.easy_news.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shimp.easy_news.user.domain.UserClicks;

public interface UserClicksRepository extends JpaRepository<UserClicks, Long> {
}
