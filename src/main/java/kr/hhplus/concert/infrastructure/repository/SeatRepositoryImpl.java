package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.domain.model.Seat;
import kr.hhplus.concert.domain.repository.SeatRepository;
import kr.hhplus.concert.infrastructure.entity.SeatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository seatJpaRepository;

    @Override
    @Transactional
    public Seat save(Seat seat) {
        SeatEntity seatEntity = seatJpaRepository.save(SeatEntity.from(seat));
        return seatEntity.of();
    }


    @Override
    public Optional<Seat> findById(Long concertScheduleId) {
        return seatJpaRepository.findById(concertScheduleId)
                .map(SeatEntity::of);
    }

}
