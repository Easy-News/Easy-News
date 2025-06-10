package shimp.easy_news.gpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import shimp.easy_news.gpt.constant.GptRole;
import shimp.easy_news.gpt.dto.ChatMessageDto;
import shimp.easy_news.gpt.dto.ChatReqDto;
import shimp.easy_news.gpt.dto.ChatResDto;
import shimp.easy_news.news.constant.SubCategory;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GptClientService {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public String callDescriptionGpt(String combinedNewsText, SubCategory category, GptRole gptRole) {

        String systemMessage = gptRole.getSystemMessage();
        String userPrompt = gptRole.formatUserPrompt(category.name(), combinedNewsText);

        // 1. 프롬프트 구성
        List<ChatMessageDto> messages = List.of(
                new ChatMessageDto("system", systemMessage),
                new ChatMessageDto("user", userPrompt)
        );

        ChatReqDto request = new ChatReqDto(model, messages);

        // 2. HTTP 요청 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<ChatReqDto> httpEntity = new HttpEntity<>(request, headers);

        // 3. GPT 호출
        ResponseEntity<ChatResDto> response = restTemplate.postForEntity(
                apiUrl,
                httpEntity,
                ChatResDto.class
        );

        assert response.getBody() != null;
        return Optional.ofNullable(response.getBody())
                .map(res -> res.getChoices().get(0).getMessage().getContent().trim())
                .orElse("GPT 응답이 없습니다.");
    }

    public String callSummaryGpt(String combinedNewsText, SubCategory category, GptRole gptRole) {
        // 1. 프롬프트 구성
        String systemMessage = gptRole.getSystemMessage();
        String userPrompt = gptRole.formatUserPrompt(category.name(), combinedNewsText);

        List<ChatMessageDto> messages = List.of(
                new ChatMessageDto("system", systemMessage),
                new ChatMessageDto("user", userPrompt.toString())
        );

        ChatReqDto request = new ChatReqDto(model, messages);

        // 2. HTTP 요청 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        HttpEntity<ChatReqDto> httpEntity = new HttpEntity<>(request, headers);

        // 3. 요청 전송
        ResponseEntity<ChatResDto> response = restTemplate.postForEntity(
                apiUrl,
                httpEntity,
                ChatResDto.class
        );

        return Optional.ofNullable(response.getBody())
                .map(res -> res.getChoices().get(0).getMessage().getContent().trim())
                .orElse("GPT 응답이 없습니다.");
    }
}

