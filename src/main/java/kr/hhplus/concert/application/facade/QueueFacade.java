package kr.hhplus.concert.application.facade;

import kr.hhplus.concert.domain.exception.ConcurrencyReservationException;
import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.service.QueueService;
import kr.hhplus.concert.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QueueFacade {
    private final QueueService queueService;
    private final UserService userService;

    @Transactional
    public Queue registerToken(Long userId, Long concertScheduleId) {
        try {
            // 유저 존재 유무 확인
            userService.CheckExistsUser(userId);

            return queueService.registerToken(userId, concertScheduleId);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ConcurrencyReservationException();
        }
    }

    public Queue getValidatedQueue(Long userId) {
        // 유저 존재 유뮤 확인
        userService.CheckExistsUser(userId);

        // 유효한 Enqueue 인지 확인
        return queueService.getValidatedQueue(userId);
    }

}
