package shimp.easy_news.news.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NewsDescriptionResDto {

    private String combinedNewsText;
    private String sourceListText;
}
