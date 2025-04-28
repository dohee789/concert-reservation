package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.infrastructure.entity.ConcertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJpaRepository extends JpaRepository<ConcertEntity, Long> {
}
