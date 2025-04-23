package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.infrastructure.entity.QueueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QueueJpaRepository extends JpaRepository<QueueEntity, Long> {
    Optional<QueueEntity> findByToken(UUID token);
}
