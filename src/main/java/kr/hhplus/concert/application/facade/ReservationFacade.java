package kr.hhplus.concert.application.facade;

import kr.hhplus.concert.application.dto.ReservationCommand;
import kr.hhplus.concert.application.dto.ReservationResult;
import kr.hhplus.concert.application.lock.DistributeLock;
import kr.hhplus.concert.domain.exception.ConcurrencyReservationException;
import kr.hhplus.concert.domain.model.common.Events;
import kr.hhplus.concert.domain.model.queue.Queue;
import kr.hhplus.concert.domain.model.reservation.Reservation;
import kr.hhplus.concert.domain.model.reservation.ReservationCompletedEvent;
import kr.hhplus.concert.domain.model.seat.Seat;
import kr.hhplus.concert.domain.service.QueueService;
import kr.hhplus.concert.domain.service.ReservationService;
import kr.hhplus.concert.domain.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationFacade {
    private final QueueService queueService;
    private final SeatService seatService;
    private final ReservationService reservationService;
    private final ApplicationEventPublisher eventPublisher;

    @DistributeLock(key = "'assign-seat:' + #command.seatId", waitTimeSec = 3, leaseTimeSec = 6)
    @Transactional
    public ReservationResult makeReservation(ReservationCommand command) {
        try {
            // 토큰 검증
            Queue queue = queueService.getValidatedQueue(command.userId());
            // 콘서트 좌석 찾기
            Seat seat = seatService.findSeat(command.concertScheduleId());
            // 좌석 점유
            seatService.assignSeat(seat.getConcertSchedule().id());
            // 예약 정보 저장
            Reservation reservation = reservationService.makeReservation(queue, seat);

            // 예약 완료 이벤트 발행 (트랜잭션 커밋 후 Kafka로 전송됨)
            eventPublisher.publishEvent(new ReservationCompletedEvent(
                    reservation.getSeat().getConcertSchedule().id(),
                    reservation.getReservedAt()
            ));

            return ReservationResult.from(reservation);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ConcurrencyReservationException();
        }
    }
}