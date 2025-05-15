package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.model.enums.QueueStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QueueRedisRepository {

    private static final String ACTIVE_QUEUE_KEY = "queue:active";
    private static final long TTL_SECONDS = 300;

    private final RedisTemplate<String, String> redisTemplate;

    public void saveQueue(Queue queue) {
        String userId = queue.getUserId().toString();
        double score = queue.getEnteredAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        redisTemplate.opsForZSet().add(ACTIVE_QUEUE_KEY, userId, score);

        Map<String, String> tokenData = Map.of(
                "token", queue.getToken(),
                "expiredAt", queue.getExpiredAt().toString()
        );

        String tokenKey = "queue:token:" + userId;
        redisTemplate.opsForHash().putAll(tokenKey, tokenData);
        redisTemplate.expire(tokenKey, Duration.ofSeconds(TTL_SECONDS));
    }

    public Optional<Queue> findQueueByUserId(Long userId) {
        String userIdStr = userId.toString();
        String tokenKey = "queue:token:" + userIdStr;

        Map<Object, Object> data = redisTemplate.opsForHash().entries(tokenKey);
        if (data.isEmpty()) return Optional.empty();

        String token = (String) data.get("token");
        String expiredAtStr = (String) data.get("expiredAt");
        LocalDateTime expiredAt = LocalDateTime.parse(expiredAtStr);

        Double score = redisTemplate.opsForZSet().score(ACTIVE_QUEUE_KEY, userIdStr);
        if (score == null) return Optional.empty();

        LocalDateTime enteredAt = Instant.ofEpochMilli(score.longValue())
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        return Optional.of(Queue.builder()
                .userId(userId)
                .token(token)
                .expiredAt(expiredAt)
                .enteredAt(enteredAt)
                .queueStatus(QueueStatus.ACTIVE)
                .build());
    }

    public Optional<Queue> findByToken(String token) {
        Set<String> userIds = redisTemplate.opsForZSet().range(ACTIVE_QUEUE_KEY, 0, -1);
        if (userIds == null) return Optional.empty();

        for (String userId : userIds) {
            String storedToken = (String) redisTemplate.opsForHash().get("queue:token:" + userId, "token");
            if (token.equals(storedToken)) {
                return findQueueByUserId(Long.valueOf(userId));
            }
        }

        return Optional.empty();
    }

    public long countActive() {
        return redisTemplate.opsForZSet().zCard(ACTIVE_QUEUE_KEY);
    }

    public long countAheadOf(Long userId) {
        Long rank = redisTemplate.opsForZSet().rank(ACTIVE_QUEUE_KEY, userId.toString());
        return rank == null ? -1L : rank;
    }

    public void remove(Long userId) {
        redisTemplate.opsForZSet().remove(ACTIVE_QUEUE_KEY, userId.toString());
        redisTemplate.delete("queue:token:" + userId);
    }

    public List<Queue> findAll() {
        Set<String> userIds = redisTemplate.opsForZSet().range(ACTIVE_QUEUE_KEY, 0, -1);
        if (userIds == null) return List.of();

        return userIds.stream()
                .map(id -> findQueueByUserId(Long.valueOf(id)))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    public Set<String> getAllUserIds() {
        return redisTemplate.opsForZSet().range("queue:active", 0, -1);
    }

    public boolean existsTokenKey(String userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("queue:token:" + userId));
    }

}
