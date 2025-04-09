package kr.hhplus.concert.infrastructure.persistence.repository;

import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.model.enums.QueueStatus;
import kr.hhplus.concert.domain.repository.QueueRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryQueueRepository implements QueueRepository {

    private final Map<Long, Queue> store = new HashMap<>();
    private final AtomicInteger sequence = new AtomicInteger(1);

    public Optional<Queue> findByUserId(Long userId) {
        return Optional.ofNullable(store.get(userId));
    }

    public void save(Queue queue) {
        if (queue.getId() == null) {
            // 새 ID 부여
            queue = Queue.builder()
                    .id(sequence.getAndIncrement())
                    .userId(queue.getUserId())
                    .token(queue.getToken())
                    .expiredAt(queue.getExpiredAt())
                    .enteredAt(queue.getEnteredAt())
                    .queueStatus(queue.getQueueStatus())
                    .build();
        }
        store.put(queue.getUserId(), queue);
    }

    public Long countActiveQueues() {
        return store.values().stream()
                .filter(Queue::isActive)
                .count();
    }

    public Integer countAheadOf(Long userId) {
        Queue EnQueue = findByUserId(userId)
                .filter(Queue::isActive)
                .orElseThrow(() -> new NoSuchElementException("유효한 ACTIVE 상태의 유저가 없습니다."));

        return (int) store.values().stream()
                .filter(Queue::isActive)
                .filter(queue -> queue.getEnteredAt().isBefore(EnQueue.getEnteredAt()))
                .count();
    }

}
