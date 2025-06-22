package shimp.easy_news.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shimp.easy_news.news.constant.NewsType;
import shimp.easy_news.news.domain.News;
import shimp.easy_news.news.repository.NewsRepository;
import shimp.easy_news.user.dto.HomeNewsResponse;
import shimp.easy_news.user.dto.NewsDto;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsCheckService {

    private final NewsRepository newsRepository;

    public HomeNewsResponse getHomeNews(int page, int size) {
        return HomeNewsResponse.builder()
//                .personalizedNews(getPersonalizedNews(userId, size))
                .headlineNews(getHeadlineNews(size))
                .realTimeNews(getRealTimeNews(size))
                .build();
    }
//
//    private List<NewsDto> getPersonalizedNews(String userId, int size) {
//        // 개인화 로직 (현재는 임시로 최신 뉴스 반환)
//        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());
//        List<News> newsList = newsRepository.findByNewsType(NewsType.PERSONALIZED, pageable);
//        return convertToDto(newsList);
//    }

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

    private NewsDto convertToDto(News news) {
        return NewsDto.builder()
                .newsId(news.getNewsId())
                .title(news.getTitle())
                .content(news.getContent())
                .summary(extractSummary(news.getContent())) // 요약 추출
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

    private String extractFirstImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }

        // 따옴표 제거
        String cleanUrl = imageUrl.replace("\"", "").trim();

        // 여러 URL이 있는 경우 첫 번째만 선택 (줄바꿈, 쉼표, 공백으로 구분)
        String[] urls = cleanUrl.split("[\\n\\r,\\s]+");

        // 첫 번째 유효한 URL 반환
        for (String url : urls) {
            url = url.trim();
            if (!url.isEmpty() && (url.startsWith("http://") || url.startsWith("https://"))) {
                return url;
            }
        }

        return null;
    }

}

