package shimp.easy_news.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shimp.easy_news.recommendation.VisitLoggingInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private VisitLoggingInterceptor visitLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 모든 요청에 대해 인터셉터 적용
        registry.addInterceptor(visitLoggingInterceptor)
                .addPathPatterns("/news/**");
    }
}