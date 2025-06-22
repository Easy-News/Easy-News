package shimp.easy_news.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import shimp.easy_news.news.constant.Category;
import shimp.easy_news.news.constant.NewsType;
import shimp.easy_news.news.constant.SubCategory;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class NewsDto {
    private Long newsId;
    private String title;
    private String content;
    private String summary;           // 요약 텍스트
    private Category category;
    private SubCategory subCategory;
    private String imageUrl;
    private String articleUrl;
    private LocalDate createdAt;
    private NewsType newsType;
}
