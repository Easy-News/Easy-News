package shimp.easy_news.domain;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import shimp.easy_news.Category;
import shimp.easy_news.NewsType;
import shimp.easy_news.SubCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
@Getter
@Setter
@Table
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long newsId;

    @Column(name = "news_type")
    @Enumerated(value = EnumType.STRING)
    private NewsType newsType;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "sub_category", nullable = false)
    private SubCategory subCategory;

    @Column(name = "article_url", nullable = false)
    private String articleUrl;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;
}
