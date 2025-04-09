package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.repository.QueueRepository;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class QueueService {
    private final QueueRepository queueRepository;
    private static final int MAX_ACTIVE_COUNT = 50;

    public Queue registerToken(Long userId) {
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

    // 앞에 있는 사람 유저수 + 본인 = 나의 순번
    public int findMyEnQueueOrder(Long userId) {
        return queueRepository.countAheadOf(userId) + 1;
    }


}
