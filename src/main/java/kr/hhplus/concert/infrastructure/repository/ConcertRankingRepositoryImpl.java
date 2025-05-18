package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.domain.repository.ConcertRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ConcertRankingRepositoryImpl implements ConcertRankingRepository {

    private final ConcertRankingRedisRepository concertRankingRedisRepository;

    @Override
    public void recordSoldOutDuration(Long concertId, Long durationSeconds) {
        concertRankingRedisRepository.addScore(concertId, durationSeconds);
    }

    @Override
    public void recordSoldOutCount(Long concertId, Integer count) {
        concertRankingRedisRepository.upsertScore(concertId, count);
    }

    @Override
    public boolean isAlreadyRanked(Long concertId) {
        return concertRankingRedisRepository.exists(concertId);
    }

    @Override
    public List<Long> getTopByDuration(Integer limit) {
        return concertRankingRedisRepository.getTopConcertsByDuration(limit);
    }

    @Override
    public Map<Long, Double> getAllDurationScores() {
        return concertRankingRedisRepository.getAllDurationScores();
    }

    @Override
    public Map<Long, Double> getAllCountScores() {
        return concertRankingRedisRepository.getAllCountScores();
    }
}

