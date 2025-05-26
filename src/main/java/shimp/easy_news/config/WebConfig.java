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
        registry.addInterceptor(visitLoggingInterceptor)
                .addPathPatterns("/news/**")  // 뉴스 관련 경로에만 적용
                .excludePathPatterns(
                        "/news/api/**",     // API 호출 제외
                        "/news/admin/**",   // 관리자 페이지 제외
                        "/**/*.css",        // CSS 파일 제외
                        "/**/*.js",         // JS 파일 제외
                        "/**/*.png",        // 이미지 파일들 제외
                        "/**/*.jpg",
                        "/**/*.jpeg",
                        "/**/*.gif",
                        "/**/*.ico",
                        "/**/*.svg"
                )
                .order(1); // 인터셉터 실행 순서 (낮을수록 먼저 실행)
    }
}