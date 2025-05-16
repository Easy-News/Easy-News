package shimp.easy_news.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shimp.easy_news.news.constant.SubCategory;
import shimp.easy_news.news.domain.News;

public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("select n.subCategory from News n where n.newsId = :id")
    SubCategory findSubCategoryById(@Param("id") Long id);
}
