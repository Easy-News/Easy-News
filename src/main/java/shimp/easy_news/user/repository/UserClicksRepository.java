package shimp.easy_news.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shimp.easy_news.user.domain.User;
import shimp.easy_news.user.domain.UserClicks;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserClicksRepository extends JpaRepository<UserClicks, Long> {

    /**
     * 사용자 ID로 UserClicks 조회
     */
    @Query("SELECT uc FROM UserClicks uc WHERE uc.user.userId = :userId")
    Optional<UserClicks> findByUserId(@Param("userId") Long userId);

    /**
     * User 객체로 UserClicks 조회
     */
    Optional<UserClicks> findByUser(User user);

    /**
     * 사용자 ID로 UserClicks 존재 여부 확인
     */
    @Query("SELECT COUNT(uc) > 0 FROM UserClicks uc WHERE uc.user.userId = :userId")
    boolean existsByUserId(@Param("userId") Long userId);

    /**
     * 총 클릭 수가 특정 값 이상인 사용자들의 UserClicks 조회
     */
    @Query("SELECT uc FROM UserClicks uc WHERE " +
            "(uc.domestic_politics_clicks + uc.election_and_presidential_clicks + " +
            "uc.international_politics_and_diplomacy_clicks + uc.economic_policy_clicks + " +
            "uc.corporate_and_industry_trends_clicks + uc.finance_and_securities_clicks + " +
            "uc.it_and_science_technology_clicks + uc.telecommunication_and_mobile_clicks + " +
            "uc.society_and_welfare_clicks + uc.incident_and_accident_clicks + " +
            "uc.legal_and_security_clicks + uc.environment_and_climate_clicks + " +
            "uc.culture_and_art_clicks + uc.entertainment_and_broadcasting_clicks + " +
            "uc.sports_clicks + uc.health_and_medical_clicks + " +
            "uc.education_and_admissions_clicks + uc.real_estate_and_construction_clicks + " +
            "uc.travel_and_leisure_clicks + uc.column_and_opinion_clicks) >= :minClicks")
    List<UserClicks> findByTotalClicksGreaterThanEqual(@Param("minClicks") int minClicks);

    /**
     * 특정 카테고리의 클릭 수가 가장 높은 사용자들 조회 (상위 N명)
     */
    @Query("SELECT uc FROM UserClicks uc ORDER BY uc.sports_clicks DESC")
    List<UserClicks> findTopBySportsClicks(org.springframework.data.domain.Pageable pageable);

    @Query("SELECT uc FROM UserClicks uc ORDER BY uc.domestic_politics_clicks DESC")
    List<UserClicks> findTopByDomesticPoliticsClicks(org.springframework.data.domain.Pageable pageable);

    @Query("SELECT uc FROM UserClicks uc ORDER BY uc.it_and_science_technology_clicks DESC")
    List<UserClicks> findTopByItAndScienceTechnologyClicks(org.springframework.data.domain.Pageable pageable);

    /**
     * 사용자 ID로 UserClicks 삭제
     */
    @Query("DELETE FROM UserClicks uc WHERE uc.user.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}