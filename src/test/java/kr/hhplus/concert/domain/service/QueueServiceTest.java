package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.model.enums.QueueStatus;
import kr.hhplus.concert.infrastructure.repository.QueueRepositoryImpl;
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
    private QueueRepositoryImpl queueRepository;

    @InjectMocks
    private QueueService queueService;

    @DisplayName("활성화된 토큰이 50개 미만이라면 토큰이 발급되고 활성열에 인입한다")
    @Test
    void GenerateToken_IfTokenCountUnder50() {
        // given
        Long userId = 1L;
        Long concertScheduleId = 1L;
        // when
        Mockito.when(queueRepository.countActiveQueues()).thenReturn(49L);
        Queue token = queueService.registerToken(userId, concertScheduleId);
        // then
        assertThat(token.getQueueStatus()).isEqualTo(QueueStatus.ACTIVE);
    }

    @DisplayName("활성화된 토큰이 50개가 초과된다면 해당 토큰은 대기열에 인입한다.")
    @Test
    void QueueStatusPending_IfTokenCountOver50() {
        // given
        Long userId = 1L;
        Long concertScheduleId = 1L;
        // when
        Mockito.when(queueRepository.countActiveQueues()).thenReturn(50L);
        Queue token = queueService.registerToken(userId, concertScheduleId);
        // then
        assertThat(token.getQueueStatus()).isEqualTo(QueueStatus.WAITING);
    }

}