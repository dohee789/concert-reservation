package kr.hhplus.concert.application.scheduler;
import kr.hhplus.concert.infrastructure.repository.QueueRedisRepository;
import org.springframework.scheduling.annotation.Scheduled;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenExpireScheduler {

    private final QueueRedisRepository queueRedisRepository;

    @Scheduled(fixedDelay = 60_000*5) // 5분마다 실행
    public void expireInactiveTokens() {
        Set<String> userIds = queueRedisRepository.getAllUserIds();

        for (String userId : userIds) {
            boolean exists = queueRedisRepository.existsTokenKey(userId);
            if (!exists) {
                log.info("만료된 유저 [{}]를 큐에서 제거합니다.", userId);
                queueRedisRepository.remove(Long.valueOf(userId));
            }
        }
    }
}

