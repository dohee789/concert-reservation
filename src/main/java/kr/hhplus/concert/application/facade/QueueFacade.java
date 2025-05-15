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
            userService.CheckExistsUser(userId);
            return queueService.registerToken(userId, concertScheduleId);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ConcurrencyReservationException();
        }
    }

    public Queue getValidatedQueue(Long userId) {
        userService.CheckExistsUser(userId);
        return queueService.getValidatedQueue(userId);
    }

    public void expireToken(Long userId) {
        queueService.expireToken(userId);
    }

    public Queue findByToken(String token) {
        return queueService.findByToken(token);
    }

}
