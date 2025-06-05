package shimp.easy_news.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shimp.easy_news.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
