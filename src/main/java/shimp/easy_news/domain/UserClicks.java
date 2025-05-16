package shimp.easy_news.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class UserClicks {

    private int domestic_politics_clicks = 0;
    private int election_and_presidential_clicks = 0;
    private int international_politics_and_diplomacy_clicks = 0;
    private int economic_policy_clicks = 0;
    private int corporate_and_industry_trends_clicks = 0;
    private int finance_and_securities_clicks = 0;
    private int it_and_science_technology_clicks = 0;
    private int telecommunication_and_mobile_clicks = 0;
    private int society_and_welfare_clicks = 0;
    private int incident_and_accident_clicks = 0;
    private int legal_and_security_clicks = 0;
    private int environment_and_climate_clicks = 0;
    private int culture_and_art_clicks = 0;
    private int entertainment_and_broadcasting_clicks = 0;
    private int sports_clicks = 0;
    private int health_and_medical_clicks = 0;
    private int education_and_admissions_clicks = 0;
    private int real_estate_and_construction_clicks = 0;
    private int travel_and_leisure_clicks = 0;
    private int column_and_opinion_clicks = 0;

    // TODO: ENUM으로 받으면 그 value++ 하게
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

    public static UserClicks ofZero() {
        return new UserClicks();
    }
}
