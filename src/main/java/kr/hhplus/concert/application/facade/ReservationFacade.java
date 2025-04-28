package kr.hhplus.concert.application.facade;

import kr.hhplus.concert.application.dto.ReservationCommand;
import kr.hhplus.concert.application.dto.ReservationResult;
import kr.hhplus.concert.domain.exception.ConcurrencyReservationException;
import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.model.Reservation;
import kr.hhplus.concert.domain.model.Seat;
import kr.hhplus.concert.domain.service.QueueService;
import kr.hhplus.concert.domain.service.ReservationService;
import kr.hhplus.concert.domain.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationFacade {
    private QueueService queueService;
    private SeatService seatService;
    private ReservationService reservationService;

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

            return ReservationResult.from(reservation);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ConcurrencyReservationException();
        }
    }
}
