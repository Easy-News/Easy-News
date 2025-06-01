package shimp.easy_news.news.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import shimp.easy_news.news.dto.GptResponseDto;
import shimp.easy_news.news.repository.NewsRepository;
import shimp.easy_news.news.service.SummaryGptService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SummaryController {

    private final NewsRepository newsRepository;
    private final SummaryGptService summaryGptService;

    @Value("${openai.api-url}")
    private String apiUrl;

    @GetMapping("/chat")
    public String summary(Model model) {
        String modelName = "gpt-3.5-turbo";

        try {
            var response = summaryGptService.chat(modelName, apiUrl);
//            return response.getChoices().get(0).getMessage().getContent();
            model.addAttribute("summary", response.getChoices().get(0).getMessage().getContent());
        } catch (Exception e) {
            model.addAttribute("summary", "요약 중 오류 발생: " + e.getMessage());
            return ("GPT 호출 오류" + e.getMessage());

        }

        return "mail"; // resources/templates/summary.html
    }

}
