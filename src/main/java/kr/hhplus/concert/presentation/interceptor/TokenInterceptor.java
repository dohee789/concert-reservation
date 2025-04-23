package kr.hhplus.concert.presentation.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.hhplus.concert.domain.service.QueueService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final QueueService queueService;

    public TokenInterceptor(QueueService queueService) {
        this.queueService = queueService;
    }

    // 요청 처리 전(preHandle)에 사용자 권한 검증, 유저가 큐에 등록되어 있는지 검증
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("User not authenticated.");
            return false;
        }

        if (queueService.getValidatedQueue(userId) != null) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("User is not in Enqueue.");
            return false;
        }

        return true;
    }
}

