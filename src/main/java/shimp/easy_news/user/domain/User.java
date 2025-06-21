package shimp.easy_news.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import shimp.easy_news.news.constant.Category;
import shimp.easy_news.news.constant.SubCategory;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "interested", nullable = false, length = 100)
    @Enumerated(value = EnumType.STRING)
    private Category interested;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "mailing_time")
    private LocalTime mailingTime;

    @Column(name = "sent_today")
    private boolean sentToday = false;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserClicks userClicks;


    /**
     * UserClicks 양방향 관계 설정
     * @param userClicks 설정할 UserClicks 객체
     */
    public void setUserClicks(UserClicks userClicks) {
        // 기존 관계 해제
        if (this.userClicks != null && this.userClicks != userClicks) {
            this.userClicks.setUser(null);
        }

        this.userClicks = userClicks;

        // 새로운 관계 설정
        if (userClicks != null && userClicks.getUser() != this) {
            userClicks.setUser(this);
        }
    }

    /**
     * UserClicks가 없으면 생성하여 반환
     * @return UserClicks 객체 (없으면 새로 생성)
     */
    public UserClicks getOrCreateUserClicks() {
        if (this.userClicks == null) {
            UserClicks clicks = new UserClicks();
            this.setUserClicks(clicks); // 양방향 관계 설정 메서드 사용
        }
        return this.userClicks;
    }

    /**
     * UserClicks 초기화 (모든 클릭 수를 0으로 설정)
     */
    public void resetUserClicks() {
        if (this.userClicks != null) {
            this.userClicks.resetAllClicks();
        } else {
            this.getOrCreateUserClicks();
        }
    }

    /**
     * 총 클릭 수 반환
     * @return 모든 카테고리의 클릭 수 합계
     */
    public int getTotalClicks() {
        return this.userClicks != null ? this.userClicks.getTotalClicks() : 0;
    }

    /**
     * 가장 많이 클릭한 카테고리 확인 (추천 시스템에 사용 가능)
     * @return 가장 관심있는 카테고리명 (간단한 예시)
     */
    public String getMostInterestedCategory() {
        if (this.userClicks == null) {
            return "UNKNOWN";
        }

        UserClicks clicks = this.userClicks;
        int maxClicks = 0;
        String mostInterested = "UNKNOWN";

        if (clicks.getDomestic_politics_clicks() > maxClicks) {
            maxClicks = clicks.getDomestic_politics_clicks();
            mostInterested = "DOMESTIC_POLITICS";
        }
        if (clicks.getSports_clicks() > maxClicks) {
            maxClicks = clicks.getSports_clicks();
            mostInterested = "SPORTS";
        }
        if (clicks.getIt_and_science_technology_clicks() > maxClicks) {
            maxClicks = clicks.getIt_and_science_technology_clicks();
            mostInterested = "IT_AND_SCIENCE_TECHNOLOGY";
        }
        if (clicks.getEntertainment_and_broadcasting_clicks() > maxClicks) {
            maxClicks = clicks.getEntertainment_and_broadcasting_clicks();
            mostInterested = "ENTERTAINMENT_AND_BROADCASTING";
        }
        // 필요에 따라 다른 카테고리들도 추가 가능

        return mostInterested;
    }

    /**
     * 사용자가 활성 사용자인지 확인 (일정 클릭 수 이상)
     * @param minClicks 최소 클릭 수
     * @return 활성 사용자 여부
     */
    public boolean isActiveUser(int minClicks) {
        return getTotalClicks() >= minClicks;
    }

    /**
     * 비밀번호 확인 (보안상 실제로는 암호화된 비밀번호와 비교해야 함)
     * @param password 확인할 비밀번호
     * @return 비밀번호 일치 여부
     */
    public boolean checkPassword(String password) {
        return this.password != null && this.password.equals(password);
    }

    /**
     * 사용자 정보 업데이트
     * @param nickname 새로운 닉네임
     * @param interested 새로운 관심 카테고리
     */
    public void updateProfile(String nickname, Category interested) {
        if (nickname != null && !nickname.trim().isEmpty()) {
            this.nickname = nickname.trim();
        }
        if (interested != null) {
            this.interested = interested;
        }
    }

    /**
     * 이메일 발송 여부를 true로 설정
     */
    public void markAsSentToday() {
        this.sentToday = true;
    }
}