package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.domain.model.queue.Queue;
import kr.hhplus.concert.domain.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QueueRepositoryImpl implements QueueRepository {

    private final QueueRedisRepository queueRedisRepository;

    @Override
    public Optional<Queue> findByUserId(Long userId) {
        return queueRedisRepository.findQueueByUserId(userId);
    }

    @Override
    public Queue save(Queue queue) {
        queueRedisRepository.saveQueue(queue);
        return queue;
    }

    @Override
    public Long countActiveQueues() {
        return queueRedisRepository.countActive();
    }

    @Override
    public Long countAheadOf(Long userId) {
        return queueRedisRepository.countAheadOf(userId);
    }

    @Override
    public List<Queue> findAll() {
        return queueRedisRepository.findAll();
    }

    @Override
    public Optional<Queue> findByToken(String token) {
        return queueRedisRepository.findByToken(token);
    }
}
