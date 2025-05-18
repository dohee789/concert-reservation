package kr.hhplus.concert.domain.repository;

import java.util.List;
import java.util.Map;

public interface ConcertRankingRepository {
    void recordSoldOutDuration(Long concertId, Long durationSeconds);
    void recordSoldOutCount(Long concertId, Integer count);
    boolean isAlreadyRanked(Long concertId);
    List<Long> getTopByDuration(Integer limit);
    Map<Long, Double> getAllDurationScores();
    Map<Long, Double> getAllCountScores();
}
