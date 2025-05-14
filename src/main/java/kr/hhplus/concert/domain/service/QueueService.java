package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueService {
    private final QueueRepository queueRepository;
    private static final Long MAX_ACTIVE_COUNT = 50L;

    public Queue registerToken(Long userId, Long concertScheduleId) {
        Queue token;

        if (queueRepository.countActiveQueues() >= MAX_ACTIVE_COUNT) {
            token = Queue.generateToken(userId);
            token.pending(); // 초과시 대기열 인입
        } else {
            token = Queue.generateToken(userId); // 활성열 인입
        }

        queueRepository.save(token);
        return token;
    }

    // ( 앞에 있는 사람 유저수 + 본인 = 나의 순번 ) <= MAX 만족시 유효한 활성열
    public Queue getValidatedQueue(Long userId) {
        Queue queue = queueRepository.findByUserId(userId)
                .filter(Queue::isActive)
                .orElseThrow(() -> new NoSuchElementException("유효한 ACTIVE 상태의 유저가 없습니다."));

        Long myOrder = queueRepository.countAheadOf(userId) + 1L;

        if (myOrder > MAX_ACTIVE_COUNT) {
            throw new IllegalStateException("활성 큐 최대 허용 인원을 초과했습니다.");
        }

        return queue;
    }

    public Queue findByToken(String token) {
        return queueRepository.findByToken(token)
                .orElseThrow(() -> new NoSuchElementException("해당 토큰에 해당하는 큐가 존재하지 않습니다."));
    }
}
