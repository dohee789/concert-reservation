package kr.hhplus.concert.application.facade;

import kr.hhplus.concert.domain.exception.UserNotFoundException;
import kr.hhplus.concert.domain.service.QueueService;
import kr.hhplus.concert.domain.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueueFacadeTest {
    @Mock
    private QueueService queueService;

    @Mock
    private UserService userService;

    @InjectMocks
    private QueueFacade queueFacade;

    @DisplayName("존재하지 않은 유저가 토큰 발급을 시도하면 UserNotFoundException 가 발생한다.")
    @Test
    void UserNotFoundExceptionOccurred_WhenInvalidUserRegisterToken() {
        // given
        Long InvalidUserId = 999L;
        // when
        doThrow(new UserNotFoundException(InvalidUserId))
                .when(userService).CheckExistsUser(InvalidUserId);
        // then
        assertThatThrownBy(() -> queueFacade.registerToken(InvalidUserId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("존재하지 않는 유저입니다. userId = " + InvalidUserId);
    }

    @Test
    @DisplayName("유저의 대기열 순서를 조회할 수 있다")
    void getUserQueueOrder_ShouldReturnCorrectOrder() {
        // given
        Long userId = 1L;
        // when
        Mockito.when(queueService.findMyEnQueueOrder(userId)).thenReturn(3);
        int myEnQueueOrder = queueFacade.findMyEnQueueOrder(userId);
        // then
        assertThat(myEnQueueOrder).isEqualTo(4);
    }

}