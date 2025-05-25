package kr.hhplus.concert.infrastructure.client;

import kr.hhplus.concert.domain.model.reservation.ReservationCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MockReservationReporter {

    public void sendReservationToDataPlatform(ReservationCompletedEvent event) {
        log.info("데이터 플랫폼에 예약정보 전송: scheduleId={}, completedAt={}",
                event.concertScheduleId(), event.reservedAt());
    }
}
