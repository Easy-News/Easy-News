package shimp.easy_news.user.domain;

import jakarta.persistence.*;
import lombok.*;
import shimp.easy_news.news.constant.SubCategory;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEmailSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "sub_category")
    private SubCategory subCategory;

    @Column(name = "scheduled_time")
    private LocalTime scheduledTime;

    @Column(name = "sent_today")
    private boolean sentToday;
}
