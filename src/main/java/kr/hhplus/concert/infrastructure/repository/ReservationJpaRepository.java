package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.infrastructure.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {
}
