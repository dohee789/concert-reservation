package kr.hhplus.concert.application.resolver;

import kr.hhplus.concert.domain.exception.InvalidTokenFormatException;
import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TokenResolver {

    private final QueueService queueService;

    public Long resolveUserId(String token) {
        try {
            return queueService.findByToken(token).getUserId();
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenFormatException("올바르지 않은 토큰 형식입니다: " + token);
        }
    }
}
