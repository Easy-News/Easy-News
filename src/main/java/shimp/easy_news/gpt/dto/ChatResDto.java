package shimp.easy_news.gpt.dto;


import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatResDto {

    private List<Choice> choices;

    @Data
    public static class Choice {
        private ChatMessageDto message;
    }
}
