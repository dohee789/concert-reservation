package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.infrastructure.entity.ConcertScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertScheduleEntity, Long> {
    List<ConcertScheduleEntity> findAllByConcertId(Long concertId);
}
