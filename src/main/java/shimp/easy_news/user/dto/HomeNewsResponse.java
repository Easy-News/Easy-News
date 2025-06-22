package shimp.easy_news.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class HomeNewsResponse {
    private List<NewsDto> personalizedNews;    // 개인화 추천
    private List<NewsDto> headlineNews;        // 헤드라인
    private List<NewsDto> realTimeNews;
}
