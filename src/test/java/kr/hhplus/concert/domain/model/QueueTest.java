package kr.hhplus.concert.domain.model;

import kr.hhplus.concert.domain.model.enums.QueueStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class QueueTest {
    @DisplayName("토큰이 발급되면 활성열에 인입하게 된다")
    @Test
    void QueueStatusActivate_WhenTokenGenerated() {
        // given
        Long userId = 1L;
        // when
        Queue token = Queue.generateToken(userId);
        // then
        assertThat(token.getQueueStatus()).isEqualTo(QueueStatus.ACTIVE);
    }

//    @DisplayName("토큰 활성화 시간이 5분 초과되면 토큰은 만료된다")
//    @Test
//    void test() {
//        // given
//        Long userId = 1L;
//        // when
//        Queue token = Queue.generateToken(userId);
//        token.expireToken(token);
//        // then
//        assertThat(token.isExpired()).isTrue();
//    }

}
