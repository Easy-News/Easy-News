package shimp.easy_news.recommendation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shimp.easy_news.user.domain.User;
import shimp.easy_news.user.domain.UserClicks;
import shimp.easy_news.user.repository.UserRepository;
import shimp.easy_news.user.repository.UserClicksRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shimp.easy_news.news.constant.SubCategory;
import shimp.easy_news.news.repository.NewsRepository;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class VisitLogService {
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final UserClicksRepository userClicksRepository;

    @Transactional
    public void logVisit(Long userId, String uri) {
        try {
            if (uri == null || !uri.startsWith("/news/article/")) {
                return;
            }
            log.warn(uri);
            Long newsId = extractNewsIdFromUri(uri);
            if (newsId == null) {
                log.warn("뉴스 ID를 추출할 수 없습니다. URI: {}", uri);
                return;
            }

            SubCategory subCategory = newsRepository.findSubCategoryById(newsId);
            if (subCategory == null) {
                log.warn("뉴스 ID {}에 대한 서브카테고리를 찾을 수 없습니다.", newsId);
                return;
            }
            log.warn(subCategory.toString());
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                log.warn("사용자 ID {}를 찾을 수 없습니다.", userId);
                return;
            }

            User user = userOptional.get();
            log.warn(user.toString());
            // UserClicks가 없으면 생성 (User의 헬퍼 메서드 사용)
            UserClicks clicks = user.getOrCreateUserClicks();

            // 새로 생성된 경우 저장
            if (clicks.getId() == null) {
                userRepository.save(user); // Cascade로 UserClicks도 함께 저장됨
            }
            log.warn(clicks.toString());
            incrementClickByCategory(clicks, subCategory);

            // UserClicks만 저장 (User는 이미 영속 상태)
            userClicksRepository.save(clicks);

            log.debug("사용자 ID: {}, 뉴스 ID: {}, 카테고리: {}에 대한 클릭이 기록되었습니다.",
                    userId, newsId, subCategory);

        } catch (Exception e) {
            log.error("방문 로그 기록 중 오류 발생. 사용자 ID: {}, URI: {}", userId, uri, e);
        }
    }

    private Long extractNewsIdFromUri(String uri) {
        try {
            int lastSlashIndex = uri.lastIndexOf('/');
            if (lastSlashIndex == -1 || lastSlashIndex == uri.length() - 1) {
                return null;
            }

            String newsIdStr = uri.substring(lastSlashIndex + 1);
            // 쿼리 파라미터가 있는 경우 제거
            int queryIndex = newsIdStr.indexOf('?');
            if (queryIndex != -1) {
                newsIdStr = newsIdStr.substring(0, queryIndex);
            }

            return Long.valueOf(newsIdStr);
        } catch (NumberFormatException e) {
            log.warn("URI에서 뉴스 ID 파싱 실패: {}", uri, e);
            return null;
        }
    }

    private void incrementClickByCategory(UserClicks clicks, SubCategory subCategory) {
        switch (subCategory) {
            case DOMESTIC_POLITICS -> clicks.incrementDomestic_politics_clicks();
            case ELECTION_AND_PRESIDENTIAL -> clicks.incrementElection_and_presidential_clicks();
            case INTERNATIONAL_POLITICS_AND_DIPLOMACY -> clicks.incrementInternational_politics_and_diplomacy_clicks();
            case ECONOMIC_POLICY -> clicks.incrementEconomic_policy_clicks();
            case CORPORATE_AND_INDUSTRY_TRENDS -> clicks.incrementCorporate_and_industry_trends_clicks();
            case FINANCE_AND_SECURITIES -> clicks.incrementFinance_and_securities_clicks();
            case IT_AND_SCIENCE_TECHNOLOGY -> clicks.incrementIt_and_science_technology_clicks();
            case TELECOMMUNICATION_AND_MOBILE -> clicks.incrementTelecommunication_and_mobile_clicks();
            case SOCIETY_AND_WELFARE -> clicks.incrementSociety_and_welfare_clicks();
            case INCIDENT_AND_ACCIDENT -> clicks.incrementIncident_and_accident_clicks();
            case LEGAL_AND_SECURITY -> clicks.incrementLegal_and_security_clicks();
            case ENVIRONMENT_AND_CLIMATE -> clicks.incrementEnvironment_and_climate_clicks();
            case CULTURE_AND_ART -> clicks.incrementCulture_and_art_clicks();
            case ENTERTAINMENT_AND_BROADCASTING -> clicks.incrementEntertainment_and_broadcasting_clicks();
            case SPORTS -> clicks.incrementSports_clicks();
            case HEALTH_AND_MEDICAL -> clicks.incrementHealth_and_medical_clicks();
            case EDUCATION_AND_ADMISSIONS -> clicks.incrementEducation_and_admissions_clicks();
            case REAL_ESTATE_AND_CONSTRUCTION -> clicks.incrementReal_estate_and_construction_clicks();
            case TRAVEL_AND_LEISURE -> clicks.incrementTravel_and_leisure_clicks();
            case COLUMN_AND_OPINION -> clicks.incrementColumn_and_opinion_clicks();
            default -> log.warn("알 수 없는 서브카테고리: {}", subCategory);
        }
    }
}