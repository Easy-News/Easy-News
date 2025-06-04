package shimp.easy_news.news.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import shimp.easy_news.news.constant.NewsType;
import shimp.easy_news.news.dto.GptRequestDto;
import shimp.easy_news.news.dto.GptResponseDto;
import shimp.easy_news.news.dto.Message;
import shimp.easy_news.news.repository.NewsRepository;
import shimp.easy_news.news.service.MailService;
import shimp.easy_news.news.service.SummaryGptService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SummaryController {

    private final NewsRepository newsRepository;
    private final SummaryGptService summaryGptService;
    private final MailService mailService;

    @Value("${openai.api-url}")
    private String apiUrl;

    @GetMapping("/chat")
    public String summary(Model model) {
        String modelName = "gpt-3.5-turbo";

        //TODO: user의 메일로
        String email = "kimjyun27@gmail.com";

        try {
            GptResponseDto response = summaryGptService.chat(modelName, apiUrl);
            String summary = response.getChoices().get(0).getMessage().getContent();
            model.addAttribute("summary", response.getChoices().get(0).getMessage().getContent());
            mailService.sendEmail(email, summary);
        } catch (Exception e) {
            model.addAttribute("summary", "요약 중 오류 발생: " + e.getMessage());

        }

        return "mail"; // resources/templates/summary.html
    }

}
