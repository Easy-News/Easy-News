package shimp.easy_news.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GptConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
