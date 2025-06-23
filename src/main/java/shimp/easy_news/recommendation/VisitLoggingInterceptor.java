package shimp.easy_news.recommendation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import shimp.easy_news.user.domain.User;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class VisitLoggingInterceptor implements HandlerInterceptor {

    private final VisitLogService visitLogService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        log.error("TEST");
        try {
            // GET 요청만 로깅 (POST 등은 제외)
            if (!"GET".equalsIgnoreCase(request.getMethod())) {
                log.info(request.getRequestURI());
                return true;
            }

            HttpSession session = request.getSession(false);
            if (session == null) {
                log.error("session is null");
                return true;
            }

            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null) {
                log.error("loginUser is null");
                return true;
            }

            String uri = request.getRequestURI();

            // 로깅 대상 URI인지 확인
            log.info("uri:{}", uri);
            if (isLoggableUri(uri)) {
                // 비동기로 로깅 처리하여 응답 성능에 영향 최소화
                visitLogService.logVisit(loginUser.getUserId(), uri);

                log.debug("방문 로그 비동기 처리 요청: 사용자 ID={}, URI={}",
                        loginUser.getUserId(), uri);
            }
            log.warn("FUCK");

        } catch (Exception e) {
            log.error("VisitLoggingInterceptor에서 예외 발생", e);
            // 인터셉터에서 예외가 발생해도 요청은 계속 진행되어야 함
        }

        return true; // 계속 진행
    }

    /**
     * 로깅 대상 URI인지 확인
     */
    private boolean isLoggableUri(String uri) {
        if (uri == null || uri.isEmpty()) {
            return false;
        }

        // 뉴스 상세 페이지만 로깅
        if (uri.startsWith("/news/article/") && uri.length() > "/news/article/".length()) {
            // 정적 리소스 제외
            return !uri.contains(".css") &&
                    !uri.contains(".js") &&
                    !uri.contains(".png") &&
                    !uri.contains(".jpg") &&
                    !uri.contains(".jpeg") &&
                    !uri.contains(".gif") &&
                    !uri.contains(".ico") &&
                    !uri.contains(".svg");
        }

        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        // 필요시 후처리 로직 추가
        if (ex != null) {
            log.error("요청 처리 중 예외 발생: {}", request.getRequestURI(), ex);
        }
    }
}