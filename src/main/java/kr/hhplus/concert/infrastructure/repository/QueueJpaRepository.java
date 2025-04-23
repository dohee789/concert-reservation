package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.infrastructure.entity.QueueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QueueJpaRepository extends JpaRepository<QueueEntity, Long> {
    Optional<QueueEntity> findByToken(String token);
}
