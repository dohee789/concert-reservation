package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.infrastructure.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatJpaRepository extends JpaRepository<SeatEntity, Long> {
}
