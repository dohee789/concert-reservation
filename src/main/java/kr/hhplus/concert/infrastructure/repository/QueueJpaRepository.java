package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.infrastructure.entity.QueueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueJpaRepository extends JpaRepository<QueueEntity, Long> {
}
