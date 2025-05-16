package shimp.easy_news.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.Getter;
import org.hibernate.annotations.Type;
import shimp.easy_news.SubCategory;
import shimp.easy_news.SubCategoryConverter;

@Entity
@Getter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Convert(converter = SubCategoryConverter.class)
    @Column(name = "interested", nullable = false)
    private SubCategory interested;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Embedded
    private UserClicks userClicks;
}
