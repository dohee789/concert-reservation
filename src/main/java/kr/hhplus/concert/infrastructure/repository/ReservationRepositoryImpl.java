package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.domain.model.Reservation;
import kr.hhplus.concert.domain.repository.ReservationRepository;
import kr.hhplus.concert.infrastructure.entity.ReservationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;


    @Override
    @Transactional
    public Reservation save(Reservation reservation) {
        ReservationEntity reservationEntity = reservationJpaRepository.save(ReservationEntity.from(reservation));
        return reservationEntity.of();
    }


    @Override
    public Optional<Reservation> findById(Long userId) {
        return reservationJpaRepository.findById(userId)
                .map(ReservationEntity::of);
    }
}
