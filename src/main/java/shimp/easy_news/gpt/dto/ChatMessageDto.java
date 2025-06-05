package shimp.easy_news.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {

    private String role; // "user" 또는 "system"
    private String content;
}
