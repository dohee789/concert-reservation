package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.infrastructure.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatJpaRepository extends JpaRepository<SeatEntity, Long> {
}
