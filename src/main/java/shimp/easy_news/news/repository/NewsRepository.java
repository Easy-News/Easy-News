package shimp.easy_news.news.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shimp.easy_news.news.constant.NewsType;
import shimp.easy_news.news.constant.SubCategory;
import shimp.easy_news.news.domain.News;
import shimp.easy_news.news.dto.SummaryRequest;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("select n.subCategory from News n where n.newsId = :id")
    SubCategory findSubCategoryById(@Param("id") Long id);

    @Query("SELECT n.content FROM News n WHERE n.newsType = :newsType ORDER BY n.createdAt DESC")
    List<String> findTop10ContentsByNewsType(@Param("newsType") NewsType newsType, Pageable pageable);

    @Query("SELECT new shimp.easy_news.news.dto.SummaryRequest(n.title, n.content) " +
            "FROM News n WHERE n.newsType = :newsType ORDER BY n.createdAt DESC")
    List<SummaryRequest> findTop3TitleContentByNewsType(@Param("newsType") NewsType newsType, Pageable pageable);
}
