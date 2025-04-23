package kr.hhplus.concert.presentation.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.hhplus.concert.application.resolver.TokenResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenResolver tokenResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String tokenValue = request.getHeader("Token");

        if (tokenValue == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token 헤더가 필요합니다.");
            return;
        }

        try {
            UUID token = UUID.fromString(tokenValue);
            Long userId = tokenResolver.resolveUserId(tokenValue);

            request.setAttribute("userId", userId);

            filterChain.doFilter(request, response);

        } catch (IllegalArgumentException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("유효하지 않은 토큰입니다.");
        }
    }
}


