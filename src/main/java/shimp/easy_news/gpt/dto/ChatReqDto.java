package shimp.easy_news.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatReqDto {

    private String model;
    private List<ChatMessageDto> messages;
}
