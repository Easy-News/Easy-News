package shimp.easy_news.recommendation;

import lombok.AllArgsConstructor;
import shimp.easy_news.user.domain.UserClicks;
import shimp.easy_news.user.repository.UserRepository;

import org.springframework.stereotype.Service;
import shimp.easy_news.news.constant.SubCategory;
import shimp.easy_news.news.repository.NewsRepository;

@Service
@AllArgsConstructor
public class VisitLogService {
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    public void logVisit(Long userId, String uri) {
        if (uri.startsWith("/news/")) {
            Long newsId = Long.valueOf(uri.substring(uri.lastIndexOf('/') + 1));
            SubCategory subCategory = newsRepository.findSubCategoryById(newsId);
            if (subCategory != null) {
                userRepository.findById(userId).ifPresent(user -> {
                    UserClicks clicks = user.getUserClicks();
                    switch (subCategory) {
                        case DOMESTIC_POLITICS:
                            clicks.incrementDomestic_politics_clicks();
                            break;
                        case ELECTION_AND_PRESIDENTIAL:
                            clicks.incrementElection_and_presidential_clicks();
                            break;
                        case INTERNATIONAL_POLITICS_AND_DIPLOMACY:
                            clicks.incrementInternational_politics_and_diplomacy_clicks();
                            break;
                        case ECONOMIC_POLICY:
                            clicks.incrementEconomic_policy_clicks();
                            break;
                        case CORPORATE_AND_INDUSTRY_TRENDS:
                            clicks.incrementCorporate_and_industry_trends_clicks();
                            break;
                        case FINANCE_AND_SECURITIES:
                            clicks.incrementFinance_and_securities_clicks();
                            break;
                        case IT_AND_SCIENCE_TECHNOLOGY:
                            clicks.incrementIt_and_science_technology_clicks();
                            break;
                        case TELECOMMUNICATION_AND_MOBILE:
                            clicks.incrementTelecommunication_and_mobile_clicks();
                            break;
                        case SOCIETY_AND_WELFARE:
                            clicks.incrementSociety_and_welfare_clicks();
                            break;
                        case INCIDENT_AND_ACCIDENT:
                            clicks.incrementIncident_and_accident_clicks();
                            break;
                        case LEGAL_AND_SECURITY:
                            clicks.incrementLegal_and_security_clicks();
                            break;
                        case ENVIRONMENT_AND_CLIMATE:
                            clicks.incrementEnvironment_and_climate_clicks();
                            break;
                        case CULTURE_AND_ART:
                            clicks.incrementCulture_and_art_clicks();
                            break;
                        case ENTERTAINMENT_AND_BROADCASTING:
                            clicks.incrementEntertainment_and_broadcasting_clicks();
                            break;
                        case SPORTS:
                            clicks.incrementSports_clicks();
                            break;
                        case HEALTH_AND_MEDICAL:
                            clicks.incrementHealth_and_medical_clicks();
                            break;
                        case EDUCATION_AND_ADMISSIONS:
                            clicks.incrementEducation_and_admissions_clicks();
                            break;
                        case REAL_ESTATE_AND_CONSTRUCTION:
                            clicks.incrementReal_estate_and_construction_clicks();
                            break;
                        case TRAVEL_AND_LEISURE:
                            clicks.incrementTravel_and_leisure_clicks();
                            break;
                        case COLUMN_AND_OPINION:
                            clicks.incrementColumn_and_opinion_clicks();
                            break;
                    }
                    user.setUserClicks(clicks);
                    userRepository.save(user);
                });
            }
        }
    }
}
