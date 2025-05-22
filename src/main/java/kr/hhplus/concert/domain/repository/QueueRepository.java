package kr.hhplus.concert.domain.repository;

import kr.hhplus.concert.domain.model.queue.Queue;

import java.util.List;
import java.util.Optional;

public interface QueueRepository {
    Optional<Queue> findByUserId(Long userId);

    Queue save(Queue queue);

    Long countActiveQueues();

    Long countAheadOf(Long userId);

    List<Queue> findAll();

    Optional<Queue> findByToken(String token);
}
