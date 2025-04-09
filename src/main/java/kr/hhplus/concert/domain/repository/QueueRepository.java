package kr.hhplus.concert.domain.repository;

import kr.hhplus.concert.domain.model.Queue;

import java.util.List;
import java.util.Optional;

public interface QueueRepository {
    Optional<Queue> findByUserId(Long userId);
    void save(Queue queue);
    Long countActiveQueues();
    Integer countAheadOf(Long userId);
    List<Queue> findAll();
}
