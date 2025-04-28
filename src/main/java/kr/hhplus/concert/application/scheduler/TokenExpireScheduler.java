package kr.hhplus.concert.application.scheduler;

import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.repository.QueueRepository;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public class TokenExpireScheduler {

    private QueueRepository queueRepository;

    @Scheduled(fixedDelay = 5000*60)
    public void expireInactiveTokens() {
        List<Queue> queues = queueRepository.findAll();

        for (Queue queue : queues) {
            if (queue.isExpired()) {
                queue.expireToken();
                queueRepository.save(queue);
            }
        }
    }
}
