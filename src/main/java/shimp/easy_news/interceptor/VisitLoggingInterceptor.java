package shimp.easy_news.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import shimp.easy_news.domain.User;

@Component
public class VisitLoggingInterceptor implements HandlerInterceptor {

//    @Autowired
//    private VisitLogService visitLogService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser != null) {
                String uri = request.getRequestURI();
//                visitLogService.logVisit(loginUser.getUserId(), uri);  // TODO: 구현해야함
            }
        }
        return true;  // 계속 진행
    }
}
