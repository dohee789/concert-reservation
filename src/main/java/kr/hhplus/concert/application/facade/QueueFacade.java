package kr.hhplus.concert.application.facade;

import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.service.QueueService;
import kr.hhplus.concert.domain.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QueueFacade {
    private final QueueService queueService;
    private final UserService userService;

    public Queue registerToken(Long userId) {
        // 유저 존재 유뮤 확인
        userService.CheckExistsUser(userId);

        return queueService.registerToken(userId);
    }

    public Queue getValidatedQueue(Long userId) {
        // 유저 존재 유뮤 확인
        userService.CheckExistsUser(userId);

        // 유효한 Enqueue 인지 확인
        return queueService.getValidatedQueue(userId);
    }

}
