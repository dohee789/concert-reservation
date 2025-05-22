package kr.hhplus.concert.presentation.event;

import kr.hhplus.concert.domain.model.reservation.ReservationCompletedEvent;
import kr.hhplus.concert.infrastructure.client.MockReservationReporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationCompletedEventListener {

    private final MockReservationReporter mockReservationReporter;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onReservationCompleted(ReservationCompletedEvent event) {
        log.info("예약 완료 이벤트 수신: {}", event);
        mockReservationReporter.sendReservationToDataPlatform(event);
    }
}
