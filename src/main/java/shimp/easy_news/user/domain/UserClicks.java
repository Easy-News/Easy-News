package shimp.easy_news.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = "user")
@Table(name = "user_clicks")
public class UserClicks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clicks_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "domestic_politics_clicks")
    private int domestic_politics_clicks = 0;

    @Column(name = "election_and_presidential_clicks")
    private int election_and_presidential_clicks = 0;

    @Column(name = "international_politics_and_diplomacy_clicks")
    private int international_politics_and_diplomacy_clicks = 0;

    @Column(name = "economic_policy_clicks")
    private int economic_policy_clicks = 0;

    @Column(name = "corporate_and_industry_trends_clicks")
    private int corporate_and_industry_trends_clicks = 0;

    @Column(name = "finance_and_securities_clicks")
    private int finance_and_securities_clicks = 0;

    @Column(name = "it_and_science_technology_clicks")
    private int it_and_science_technology_clicks = 0;

    @Column(name = "telecommunication_and_mobile_clicks")
    private int telecommunication_and_mobile_clicks = 0;

    @Column(name = "society_and_welfare_clicks")
    private int society_and_welfare_clicks = 0;

    @Column(name = "incident_and_accident_clicks")
    private int incident_and_accident_clicks = 0;

    @Column(name = "legal_and_security_clicks")
    private int legal_and_security_clicks = 0;

    @Column(name = "environment_and_climate_clicks")
    private int environment_and_climate_clicks = 0;

    @Column(name = "culture_and_art_clicks")
    private int culture_and_art_clicks = 0;

    @Column(name = "entertainment_and_broadcasting_clicks")
    private int entertainment_and_broadcasting_clicks = 0;

    @Column(name = "sports_clicks")
    private int sports_clicks = 0;

    @Column(name = "health_and_medical_clicks")
    private int health_and_medical_clicks = 0;

    @Column(name = "education_and_admissions_clicks")
    private int education_and_admissions_clicks = 0;

    @Column(name = "real_estate_and_construction_clicks")
    private int real_estate_and_construction_clicks = 0;

    @Column(name = "travel_and_leisure_clicks")
    private int travel_and_leisure_clicks = 0;

    @Column(name = "column_and_opinion_clicks")
    private int column_and_opinion_clicks = 0;

    // 생성자 추가 (User와 함께 생성할 때 사용)
    public UserClicks(User user) {
        this.user = user;
        // 모든 클릭 수를 0으로 초기화 (이미 필드에서 기본값 설정됨)
    }

    public void incrementDomestic_politics_clicks() {
        this.domestic_politics_clicks++;
    }

    public void incrementElection_and_presidential_clicks() {
        this.election_and_presidential_clicks++;
    }

    public void incrementInternational_politics_and_diplomacy_clicks() {
        this.international_politics_and_diplomacy_clicks++;
    }

    public void incrementEconomic_policy_clicks() {
        this.economic_policy_clicks++;
    }

    public void incrementCorporate_and_industry_trends_clicks() {
        this.corporate_and_industry_trends_clicks++;
    }

    public void incrementFinance_and_securities_clicks() {
        this.finance_and_securities_clicks++;
    }

    public void incrementIt_and_science_technology_clicks() {
        this.it_and_science_technology_clicks++;
    }

    public void incrementTelecommunication_and_mobile_clicks() {
        this.telecommunication_and_mobile_clicks++;
    }

    public void incrementSociety_and_welfare_clicks() {
        this.society_and_welfare_clicks++;
    }

    public void incrementIncident_and_accident_clicks() {
        this.incident_and_accident_clicks++;
    }

    public void incrementLegal_and_security_clicks() {
        this.legal_and_security_clicks++;
    }

    public void incrementEnvironment_and_climate_clicks() {
        this.environment_and_climate_clicks++;
    }

    public void incrementCulture_and_art_clicks() {
        this.culture_and_art_clicks++;
    }

    public void incrementEntertainment_and_broadcasting_clicks() {
        this.entertainment_and_broadcasting_clicks++;
    }

    public void incrementSports_clicks() {
        this.sports_clicks++;
    }

    public void incrementHealth_and_medical_clicks() {
        this.health_and_medical_clicks++;
    }

    public void incrementEducation_and_admissions_clicks() {
        this.education_and_admissions_clicks++;
    }

    public void incrementReal_estate_and_construction_clicks() {
        this.real_estate_and_construction_clicks++;
    }

    public void incrementTravel_and_leisure_clicks() {
        this.travel_and_leisure_clicks++;
    }

    public void incrementColumn_and_opinion_clicks() {
        this.column_and_opinion_clicks++;
    }

    // 전체 클릭 수 계산
    public int getTotalClicks() {
        return domestic_politics_clicks + election_and_presidential_clicks +
                international_politics_and_diplomacy_clicks + economic_policy_clicks +
                corporate_and_industry_trends_clicks + finance_and_securities_clicks +
                it_and_science_technology_clicks + telecommunication_and_mobile_clicks +
                society_and_welfare_clicks + incident_and_accident_clicks +
                legal_and_security_clicks + environment_and_climate_clicks +
                culture_and_art_clicks + entertainment_and_broadcasting_clicks +
                sports_clicks + health_and_medical_clicks +
                education_and_admissions_clicks + real_estate_and_construction_clicks +
                travel_and_leisure_clicks + column_and_opinion_clicks;
    }

    // 모든 클릭 수를 0으로 초기화
    public void resetAllClicks() {
        this.domestic_politics_clicks = 0;
        this.election_and_presidential_clicks = 0;
        this.international_politics_and_diplomacy_clicks = 0;
        this.economic_policy_clicks = 0;
        this.corporate_and_industry_trends_clicks = 0;
        this.finance_and_securities_clicks = 0;
        this.it_and_science_technology_clicks = 0;
        this.telecommunication_and_mobile_clicks = 0;
        this.society_and_welfare_clicks = 0;
        this.incident_and_accident_clicks = 0;
        this.legal_and_security_clicks = 0;
        this.environment_and_climate_clicks = 0;
        this.culture_and_art_clicks = 0;
        this.entertainment_and_broadcasting_clicks = 0;
        this.sports_clicks = 0;
        this.health_and_medical_clicks = 0;
        this.education_and_admissions_clicks = 0;
        this.real_estate_and_construction_clicks = 0;
        this.travel_and_leisure_clicks = 0;
        this.column_and_opinion_clicks = 0;
    }

    public static UserClicks ofZero() {
        return new UserClicks();
    }

    public static UserClicks ofZero(User user) {
        return new UserClicks(user);

    }
}