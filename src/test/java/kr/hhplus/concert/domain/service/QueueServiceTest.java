package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.model.enums.QueueStatus;
import kr.hhplus.concert.domain.repository.QueueRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class QueueServiceTest {
    @Mock
    private QueueRepository queueRepository;

    @InjectMocks
    private QueueService queueService;

    @DisplayName("활성화된 토큰이 50개 미만이라면 토큰이 발급되고 활성열에 인입한다")
    @Test
    void GenerateToken_IfTokenCountUnder50() {
        // given
        Long userId = 1L;
        // when
        Mockito.when(queueRepository.countActiveQueues()).thenReturn(49L);
        Queue token = queueService.registerToken(userId);
        // then
        assertThat(token.getQueueStatus()).isEqualTo(QueueStatus.ACTIVE);
    }

    @DisplayName("활성화된 토큰이 50개가 초과된다면 해당 토큰은 대기열에 인입한다.")
    @Test
    void QueueStatusPending_IfTokenCountOver50() {
        // given
        Long userId = 1L;
        // when
        Mockito.when(queueRepository.countActiveQueues()).thenReturn(50L);
        Queue token = queueService.registerToken(userId);
        // then
        assertThat(token.getQueueStatus()).isEqualTo(QueueStatus.WAITING);
    }

    @Test
    @DisplayName("대기열에서 나의 순서는 앞 유저들의 순서에 1을 더한 것이다")
    void findMyEnQueueOrder_ShouldReturnCorrectOrder() {
        // given
        Long userId1 = 1L;
        Long userId2 = 2L;
        queueService.registerToken(userId1);
        queueService.registerToken(userId2);
        Mockito.when(queueRepository.countAheadOf(userId1)).thenReturn(0); // userId1 앞엔 아무도 없음
        Mockito.when(queueRepository.countAheadOf(userId2)).thenReturn(1); // userId2 앞에 userId1 있음
        // when
        // then
        assertThat(queueService.findMyEnQueueOrder(userId1)).isEqualTo(1);
        assertThat(queueService.findMyEnQueueOrder(userId2)).isEqualTo(2);
    }





}