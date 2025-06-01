package shimp.easy_news.news.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class GptResponseDto {
    private List<Choice> choices;
    private String id;
    private String model;
    private String object;
}


