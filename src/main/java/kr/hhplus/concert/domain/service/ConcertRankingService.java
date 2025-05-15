package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.Reservation;
import kr.hhplus.concert.domain.repository.ConcertRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConcertRankingService {
    private final ConcertRankingRepository concertRankingRepository;

    public void tryRecordSoldOut(Reservation reservation, LocalDateTime soldOutAt) {
        Long concertId = reservation.getSeat().getConcertSchedule().concert().id();
        LocalDateTime openedAt = reservation.getSeat().getConcertSchedule().openedAt();

        if (concertRankingRepository.isAlreadyRanked(concertId)) {
            concertRankingRepository.recordSoldOutCount(concertId,1);
        }
        Long durationSeconds = Duration.between(openedAt, soldOutAt).toSeconds();
        concertRankingRepository.recordSoldOutDuration(concertId, durationSeconds);
    }

    public List<Long> getTopByFastestSold(Integer limit) {
        return concertRankingRepository.getTopByDuration(limit);
    }

    public List<Long> getTopByCombinedScore(Integer limit, double weightDuration, double weightCount) {
        Map<Long, Double> durationMap = concertRankingRepository.getAllDurationScores();
        Map<Long, Double> countMap = concertRankingRepository.getAllCountScores();

        Map<Long, Double> combined = new HashMap<>();

        for (Long concertId : durationMap.keySet()) {
            double durationScore = durationMap.getOrDefault(concertId, Double.MAX_VALUE);
            double countScore = countMap.getOrDefault(concertId, 0.0);

            double score = durationScore * weightDuration - countScore * weightCount;
            combined.put(concertId, score);
        }

        return combined.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }


}

