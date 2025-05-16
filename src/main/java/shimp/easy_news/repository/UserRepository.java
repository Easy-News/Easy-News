package shimp.easy_news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shimp.easy_news.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
