package kr.hhplus.concert.application.resolver;

import kr.hhplus.concert.domain.exception.InvalidTokenFormatException;
import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenResolver {

    private final QueueService queueService;

    public Long resolveUserId(String tokenValue) {
        UUID token;
        try {
            token = UUID.fromString(tokenValue);
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenFormatException("올바르지 않은 토큰 형식입니다: " + tokenValue);
        }

        Queue queue = queueService.findByToken(token);
        return queue.getUserId();
    }
}
