package shimp.easy_news.interceptor;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import shimp.easy_news.domain.News;
import shimp.easy_news.SubCategory;
import shimp.easy_news.repository.NewsRepository;

@Service
public class VisitLogService {
    private final NewsRepository newsRepository;

    @Autowired
    public VisitLogService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public void logVisit(Long userId, String uri) {
        if (uri.startsWith("/news/")) {
            Long newsId = Long.valueOf(uri.substring(uri.lastIndexOf('/') + 1));
            SubCategory subCategory = newsRepository.findSubCategoryById(newsId);
        }
    }
}
