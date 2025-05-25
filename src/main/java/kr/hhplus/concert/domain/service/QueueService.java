package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.queue.Queue;
import kr.hhplus.concert.domain.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;

    private static final long MAX_ACTIVE_COUNT = 50;

    public Queue registerToken(Long userId, Long concertScheduleId) {
        boolean alreadyExists = queueRepository.findByUserId(userId).isPresent();
        if (alreadyExists) {
            throw new IllegalStateException("이미 큐에 등록된 사용자입니다.");
        }

        Queue queue = Queue.generateToken(userId);

        if (queueRepository.countActiveQueues() >= MAX_ACTIVE_COUNT) {
            queue.pending();
        }

        return queueRepository.save(queue);
    }

    public Queue getValidatedQueue(Long userId) {
        Queue queue = queueRepository.findByUserId(userId)
                .filter(Queue::isActive)
                .orElseThrow(() -> new NoSuchElementException("유효한 ACTIVE 상태의 큐가 존재하지 않습니다."));

        Long myOrder = queueRepository.countAheadOf(userId) + 1L;
        if (myOrder > MAX_ACTIVE_COUNT) {
            throw new IllegalStateException("활성 큐 최대 허용 인원을 초과했습니다.");
        }

        return queue;
    }

    public void expireToken(Long userId) {
        queueRepository.findByUserId(userId).ifPresent(queue -> {
            if (queue.isExpired()) {
                queue.expireToken();
            }
            queueRepository.save(queue);
        });
    }


    public Queue findByToken(String token) {
        return queueRepository.findByToken(token)
                .orElseThrow(() -> new NoSuchElementException("해당 토큰에 해당하는 큐가 존재하지 않습니다."));
    }
}

