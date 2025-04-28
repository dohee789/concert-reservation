package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.model.enums.QueueStatus;
import kr.hhplus.concert.domain.repository.QueueRepository;
import kr.hhplus.concert.infrastructure.entity.QueueEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.*;

@RequiredArgsConstructor
public class QueueRepositoryImpl implements QueueRepository {

    private final QueueJpaRepository queueJpaRepository;

    @Override
    public Optional<Queue> findByUserId(Long userId) {
        return queueJpaRepository.findById(userId)
                .map(QueueEntity::of);
    }

    @Override
    @Transactional
    public Queue save(Queue queue) {
        QueueEntity queueEntity = queueJpaRepository.save(QueueEntity.from(queue));
        return queueEntity.of();
    }

    @Override
    public Long countActiveQueues() {
        return queueJpaRepository.findAll().stream()
                .filter(q -> q.getQueueStatus() == QueueStatus.ACTIVE)
                .count();
    }

    @Override
    public Long countAheadOf(Long userId) {
        QueueEntity enqueued = queueJpaRepository.findById(userId)
                .filter(q -> q.getQueueStatus() == QueueStatus.ACTIVE)
                .orElseThrow(() -> new NoSuchElementException("유효한 ACTIVE 상태의 유저가 아닙니다."));

        return queueJpaRepository.findAll().stream()
                .filter(q -> q.getQueueStatus() == QueueStatus.ACTIVE)
                .filter(q -> q.getEnteredAt().isBefore(enqueued.getEnteredAt()))
                .count();
    }

    @Override
    public List<Queue> findAll() {
        return queueJpaRepository.findAll().stream()
                .map(QueueEntity::of)
                .toList();
    }
}

