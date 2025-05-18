package kr.hhplus.concert.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConcertRankingRedisRepository {

    private static final String KEY_DURATION = "concert:ranking:fastest-sold";
    private static final String KEY_COUNT = "concert:ranking:most-sold";
    private final RedisTemplate<String, String> redisTemplate;

    public void addScore(Long concertId, Long durationSeconds) {
        String member = "concert:" + concertId;
        redisTemplate.opsForZSet().add(KEY_DURATION, member, durationSeconds);
    }

    public void upsertScore(Long concertId, Integer count) {
        String member = "concert:" + concertId;
        redisTemplate.opsForZSet().incrementScore(KEY_COUNT, member, count);
    }

    public boolean exists(Long concertId) {
        String member = "concert:" + concertId;
        return redisTemplate.opsForZSet().score(KEY_DURATION, member) != null;
    }

    public List<Long> getTopConcertsByDuration(Integer limit) {
        Set<String> rawMembers = redisTemplate.opsForZSet().range(KEY_DURATION, 0, limit - 1);
        if (rawMembers == null) return List.of();

        return rawMembers.stream()
                .map(member -> {
                    String[] parts = member.split(":");
                    return parts.length == 2 ? Long.valueOf(parts[1]) : null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public Map<Long, Double> getAllDurationScores() {
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet().rangeWithScores(KEY_DURATION, 0, -1);
        if (tuples == null) return Map.of();

        return tuples.stream()
                .filter(t -> t.getValue() != null && t.getScore() != null)
                .collect(Collectors.toMap(
                        t -> Long.parseLong(t.getValue().split(":")[1]),
                        ZSetOperations.TypedTuple::getScore
                ));
    }

    public Map<Long, Double> getAllCountScores() {
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet().rangeWithScores(KEY_COUNT, 0, -1);
        if (tuples == null) return Map.of();

        return tuples.stream()
                .filter(t -> t.getValue() != null && t.getScore() != null)
                .collect(Collectors.toMap(
                        t -> Long.parseLong(t.getValue().split(":")[1]),
                        ZSetOperations.TypedTuple::getScore
                ));
    }
}


