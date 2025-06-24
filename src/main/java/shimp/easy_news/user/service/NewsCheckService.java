package shimp.easy_news.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shimp.easy_news.news.constant.NewsType;
import shimp.easy_news.news.constant.SubCategory;
import shimp.easy_news.news.domain.News;
import shimp.easy_news.news.repository.NewsRepository;
import shimp.easy_news.user.domain.User;
import shimp.easy_news.user.domain.UserClicks;
import shimp.easy_news.user.dto.HomeNewsResponse;
import shimp.easy_news.user.dto.NewsDto;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsCheckService {

    private final NewsRepository newsRepository;

    public HomeNewsResponse getHomeNews(int page, int size, User user) {
        return HomeNewsResponse.builder()
                .personalizedNews(getPersonalizedNews(user, size))
                .headlineNews(getHeadlineNews(size))
                .realTimeNews(getRealTimeNews(size))
                .build();
    }

    private List<NewsDto> getPersonalizedNews(User user, int size) {
        if (user == null || user.getUserClicks() == null) {
            // 비로그인 사용자는 최신 뉴스 반환
            Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());
            List<News> newsList = newsRepository.findByNewsType(NewsType.HEADLINE, pageable);
            return convertToDto(newsList);
        }

        // 사용자 클릭 데이터 기반으로 선호 카테고리 분석
        List<SubCategory> preferredCategories = analyzeUserPreferences(user.getUserClicks());

        // 선호 카테고리 기반 뉴스 조회
        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());
        List<News> personalizedNews = newsRepository.findBySubCategoryInOrderByCreatedAtDesc(
                preferredCategories, pageable);

        return convertToDto(personalizedNews);
    }

    public HomeNewsResponse getHomeNewsWithPaging(int headlinePage, int personalizedPage,
                                                  int size, User user) {

        // 헤드라인 뉴스 페이징 (기존 getHeadlineNews 로직 활용)
        Pageable headlinePageable = PageRequest.of(headlinePage, size, Sort.by("createdAt").descending());
        Page<News> headlineNewsPage = newsRepository.findPageByNewsType(NewsType.HEADLINE, headlinePageable);
        List<NewsDto> headlineNews = convertToDto(headlineNewsPage.getContent());

        // 개인화 추천 뉴스 페이징 (기존 getPersonalizedNews 로직 활용)
        List<NewsDto> personalizedNews;
        int personalizedTotalPages;

        if (user == null || user.getUserClicks() == null) {
            // 비로그인 사용자는 헤드라인 뉴스 사용
            personalizedNews = headlineNews;
            personalizedTotalPages = headlineNewsPage.getTotalPages();
        } else {
            // 기존 analyzeUserPreferences 함수 활용
            List<SubCategory> preferredCategories = analyzeUserPreferences(user.getUserClicks());

            if (preferredCategories.isEmpty()) {
                personalizedNews = headlineNews;
                personalizedTotalPages = headlineNewsPage.getTotalPages();
            } else {
                Pageable personalizedPageable = PageRequest.of(personalizedPage, size, Sort.by("createdAt").descending());
                List<News> personalizedNewsList = newsRepository.findBySubCategoryInOrderByCreatedAtDesc(
                        preferredCategories, personalizedPageable);
                personalizedNews = convertToDto(personalizedNewsList);

                // 개인화 뉴스의 총 페이지 수 계산 (근사치)
                personalizedTotalPages = Math.max(1, (int) Math.ceil((double) personalizedNewsList.size() / size));
            }
        }

        // 실시간 뉴스 (기존 getRealTimeNews 함수 활용)
        List<NewsDto> realTimeNews = getRealTimeNews(size);

        return HomeNewsResponse.builder()
                .personalizedNews(personalizedNews)
                .headlineNews(headlineNews)
                .realTimeNews(realTimeNews)
                .headlineTotalPages(headlineNewsPage.getTotalPages())
                .personalizedTotalPages(personalizedTotalPages)
                .build();
    }

    public HomeNewsResponse getHomeNewsForClientPaging(User user) {
        int pageSize = 25; // 25개씩 불러오기

        // 헤드라인 뉴스 25개
        Pageable headlinePageable = PageRequest.of(0, pageSize, Sort.by("createdAt").descending());
        List<News> headlineNewsList = newsRepository.findByNewsType(NewsType.HEADLINE, headlinePageable);
        List<NewsDto> headlineNews = convertToDto(headlineNewsList);

        // 개인화 추천 뉴스 25개
        List<NewsDto> personalizedNews;
        if (user == null || user.getUserClicks() == null) {
            personalizedNews = headlineNews; // 헤드라인과 동일
        } else {
            List<SubCategory> preferredCategories = analyzeUserPreferences(user.getUserClicks());
            if (preferredCategories.isEmpty()) {
                personalizedNews = headlineNews;
            } else {
                Pageable personalizedPageable = PageRequest.of(0, pageSize, Sort.by("createdAt").descending());
                List<News> personalizedNewsList = newsRepository.findBySubCategoryInOrderByCreatedAtDesc(
                        preferredCategories, personalizedPageable);
                personalizedNews = convertToDto(personalizedNewsList);
            }
        }

        // 실시간 뉴스 25개
        Pageable realTimePageable = PageRequest.of(0, pageSize, Sort.by("createdAt").descending());
        List<News> realTimeNewsList = newsRepository.findByNewsType(NewsType.LIVE, realTimePageable);
        List<NewsDto> realTimeNews = convertToDto(realTimeNewsList);

        return HomeNewsResponse.builder()
                .personalizedNews(personalizedNews)
                .headlineNews(headlineNews)
                .realTimeNews(realTimeNews)
                .build();
    }


    private List<SubCategory> analyzeUserPreferences(UserClicks userClicks) {
        List<SubCategory> preferences = new ArrayList<>();

        Map<SubCategory, Integer> clickCounts = new HashMap<>();

        clickCounts.put(SubCategory.DOMESTIC_POLITICS, userClicks.getDomestic_politics_clicks());
        clickCounts.put(SubCategory.ELECTION_AND_PRESIDENTIAL, userClicks.getElection_and_presidential_clicks());
        clickCounts.put(SubCategory.INTERNATIONAL_POLITICS_AND_DIPLOMACY, userClicks.getInternational_politics_and_diplomacy_clicks());
        clickCounts.put(SubCategory.ECONOMIC_POLICY, userClicks.getEconomic_policy_clicks());
        clickCounts.put(SubCategory.CORPORATE_AND_INDUSTRY_TRENDS, userClicks.getCorporate_and_industry_trends_clicks());
        clickCounts.put(SubCategory.FINANCE_AND_SECURITIES, userClicks.getFinance_and_securities_clicks());
        clickCounts.put(SubCategory.IT_AND_SCIENCE_TECHNOLOGY, userClicks.getIt_and_science_technology_clicks());
        clickCounts.put(SubCategory.TELECOMMUNICATION_AND_MOBILE, userClicks.getTelecommunication_and_mobile_clicks());
        clickCounts.put(SubCategory.SOCIETY_AND_WELFARE, userClicks.getSociety_and_welfare_clicks());
        clickCounts.put(SubCategory.INCIDENT_AND_ACCIDENT, userClicks.getIncident_and_accident_clicks());
        clickCounts.put(SubCategory.LEGAL_AND_SECURITY, userClicks.getLegal_and_security_clicks());
        clickCounts.put(SubCategory.ENVIRONMENT_AND_CLIMATE, userClicks.getEnvironment_and_climate_clicks());
        clickCounts.put(SubCategory.CULTURE_AND_ART, userClicks.getCulture_and_art_clicks());
        clickCounts.put(SubCategory.ENTERTAINMENT_AND_BROADCASTING, userClicks.getEntertainment_and_broadcasting_clicks());
        clickCounts.put(SubCategory.SPORTS, userClicks.getSports_clicks());
        clickCounts.put(SubCategory.HEALTH_AND_MEDICAL, userClicks.getHealth_and_medical_clicks());
        clickCounts.put(SubCategory.EDUCATION_AND_ADMISSIONS, userClicks.getEducation_and_admissions_clicks());
        clickCounts.put(SubCategory.REAL_ESTATE_AND_CONSTRUCTION, userClicks.getReal_estate_and_construction_clicks());
        clickCounts.put(SubCategory.TRAVEL_AND_LEISURE, userClicks.getTravel_and_leisure_clicks());
        clickCounts.put(SubCategory.COLUMN_AND_OPINION, userClicks.getColumn_and_opinion_clicks());


        return clickCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .sorted(Map.Entry.<SubCategory, Integer>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private List<NewsDto> getHeadlineNews(int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());
        List<News> newsList = newsRepository.findByNewsType(NewsType.HEADLINE, pageable);
        for (News news : newsList) {
            System.out.println("Image URL: " + news.getImageUrl());
        }
        return convertToDto(newsList);
    }

    private List<NewsDto> getRealTimeNews(int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());
        List<News> newsList = newsRepository.findByNewsType(NewsType.LIVE, pageable);
        for (News news : newsList) {
            System.out.println("realtime: " + news.getContent());
        }
        return convertToDto(newsList);
    }

    private List<NewsDto> convertToDto(List<News> newsList) {
        return newsList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public NewsDto convertToDto(News news) {
        return NewsDto.builder()
                .newsId(news.getNewsId())
                .title(news.getTitle())
                .content(news.getContent())
                .summary(extractSummary(news.getContent()))
                .category(news.getCategory())
                .subCategory(news.getSubCategory())
                .imageUrl(extractFirstImageUrl(news.getImageUrl()))
                .articleUrl(news.getArticleUrl())
                .createdAt(news.getCreatedAt())
                .newsType(news.getNewsType())
                .build();
    }

    private String extractSummary(String content) {
        if (content == null || content.length() <= 100) {
            return content;
        }
        return content.substring(0, 100) + "...";
    }

    /**
     * 이미지 URL 문자열에서 모든 유효한 이미지 URL을 추출
     */
    public List<String> extractAllImageUrls(String imageUrl) {
        List<String> imageUrls = new ArrayList<>();

        if (imageUrl == null || imageUrl.isEmpty()) {
            return imageUrls;
        }

        String cleanUrl = imageUrl.replace("\"", "").trim();

        String[] urls = cleanUrl.split("[\\n\\r,\\s]+");
        for (String url : urls) {
            url = url.trim();
            if (!url.isEmpty() && (url.startsWith("http://") || url.startsWith("https://"))) {
                imageUrls.add(url);
            }
        }

        return imageUrls;
    }

    /**
     * 첫 번째 이미지 URL만 추출
     */
    private String extractFirstImageUrl(String imageUrl) {
        List<String> urls = extractAllImageUrls(imageUrl);
        return urls.isEmpty() ? null : urls.get(0);
    }


}

