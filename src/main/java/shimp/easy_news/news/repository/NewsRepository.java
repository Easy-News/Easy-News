package shimp.easy_news.news.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shimp.easy_news.news.constant.Category;
import shimp.easy_news.news.constant.NewsType;
import shimp.easy_news.news.constant.SubCategory;
import shimp.easy_news.news.domain.News;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("select n.subCategory from News n where n.newsId = :id")
    SubCategory findSubCategoryById(@Param("id") Long id);

    List<News> findTop10ByCategoryOrderByCreatedAtDesc(Category category);

    List<News> findTop10ByNewsTypeOrderByCreatedAtDesc(NewsType newsType);

    List<News> findByNewsType(NewsType newsType, Pageable pageable);

    List<News> findBySubCategoryInOrderByCreatedAtDesc(List<SubCategory> subCategories, Pageable pageable);

    Page<News> findByCategory(Category category, Pageable pageable);

    Page<News> findPageByNewsType(NewsType newsType, Pageable pageable);

}
