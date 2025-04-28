package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.domain.model.ConcertSchedule;
import kr.hhplus.concert.domain.repository.ConcertScheduleRepository;
import kr.hhplus.concert.infrastructure.entity.ConcertScheduleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertScheduleRepositoryImpl implements ConcertScheduleRepository {

    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;

    @Override
    public List<ConcertSchedule> findConcertSchedulesById(Long concertId) {
        return concertScheduleJpaRepository.findAllByConcertId(concertId).stream()
                .map(ConcertScheduleEntity::of)
                .toList();
    }
}
