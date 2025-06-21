package shimp.easy_news.news.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shimp.easy_news.news.constant.Category;
import shimp.easy_news.news.constant.NewsType;
import shimp.easy_news.news.domain.News;
import shimp.easy_news.news.dto.NewsDescriptionResDto;
import shimp.easy_news.news.dto.NewsSummaryResDto;
import shimp.easy_news.news.repository.NewsRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsDescriptionResDto buildDescriptionDataByCategory(Category category) {

        // 1. 뉴스 가져오기
        List<News> latestNewsList = newsRepository.findTop10ByCategoryOrderByCreatedAtDesc(category);

        StringBuilder newsContextBuilder = new StringBuilder();
        StringBuilder sourceListBuilder = new StringBuilder("출처:\n");

        for (News news : latestNewsList) {
            newsContextBuilder.append("제목: ").append(news.getTitle()).append("\n");
            newsContextBuilder.append("본문: ").append(news.getContent()).append("\n\n");

            sourceListBuilder.append("- ").append(news.getArticleUrl()).append("\n");
        }
        return NewsDescriptionResDto.builder()
                .combinedNewsText(newsContextBuilder.toString())
                .sourceListText(sourceListBuilder.toString())
                .build();
    }

    // TODO : buildSummarization 구현
    public NewsSummaryResDto buildSummaryData() {
        List<News> latestNewsList = newsRepository.findTop10ByNewsTypeOrderByCreatedAtDesc(NewsType.HEADLINE);

        StringBuilder summaryTargetBuilder = new StringBuilder();
//        StringBuilder sourceListBuilder = new StringBuilder("출처:\n");

        for (News news : latestNewsList) {
            summaryTargetBuilder.append("제목: ").append(news.getTitle()).append("\n");
            summaryTargetBuilder.append("본문: ").append(news.getContent()).append("\n\n");

//            sourceListBuilder.append("- ").append(news.getArticleUrl()).append("\n");
        }

        return NewsSummaryResDto.builder()
                .combinedNewsText(summaryTargetBuilder.toString())
//                .sourceListText(sourceListBuilder.toString())
                .build();
    }

}
