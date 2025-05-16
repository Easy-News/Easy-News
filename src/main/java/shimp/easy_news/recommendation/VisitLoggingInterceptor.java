package shimp.easy_news.recommendation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import shimp.easy_news.user.domain.User;

@Component
@AllArgsConstructor
public class VisitLoggingInterceptor implements HandlerInterceptor {

    private VisitLogService visitLogService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser != null) {
                String uri = request.getRequestURI();
                visitLogService.logVisit(loginUser.getUserId(), uri);
            }
        }
        return true;  // 계속 진행
    }
}
