package shimp.easy_news.news.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;
import shimp.easy_news.news.dto.GptRequestDto;
import shimp.easy_news.news.dto.GptResponseDto;
import shimp.easy_news.news.dto.Message;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SummaryGptService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    public GptResponseDto chat(String model, String endpointCharged) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("당신은 신문 기사 내용을 빠르게 요약해주는 AI입니다. ")
                .append("다음 사용자의 요청에 따라 10개의 뉴스 본문을 각각의 기사마다 한 줄로 요약해주세요. ")
                .append("총 10문장의 요약문이 생성됩니다. ")
                .append("중요한 정보나 사건의 핵심 내용을 포함해 작성하세요.");
        List<Message> prompts = List.of(new Message("user", prompt.toString()));
        GptRequestDto request = new GptRequestDto(model, prompts, 1, 256, 1, 0, 0);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GptRequestDto> entity = new HttpEntity<>(request, headers);

        // OpenAI server로 restTemplate을 통해 request를 보내고 response를 받는다.
        GptResponseDto gptResponse = restTemplate.exchange(
                endpointCharged, HttpMethod.POST, entity, GptResponseDto.class).getBody();
        if (gptResponse != null) {
            return gptResponse;
        } else {
            throw new RuntimeException("Error parsing response from OpenAI Server");
        }
    }
}
