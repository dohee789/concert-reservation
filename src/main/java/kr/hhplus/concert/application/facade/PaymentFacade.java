package kr.hhplus.concert.application.facade;

import kr.hhplus.concert.domain.model.Payment;
import kr.hhplus.concert.domain.model.Reservation;
import kr.hhplus.concert.domain.model.Seat;
import kr.hhplus.concert.domain.service.PaymentService;
import kr.hhplus.concert.domain.service.QueueService;
import kr.hhplus.concert.domain.service.ReservationService;
import kr.hhplus.concert.domain.service.SeatService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentFacade {

    private final PaymentService paymentService;
    private final ReservationService reservationService;
    private final QueueService queueService;
    private final SeatService seatService;

    public Payment Payment(Long userId, Integer reservationId) {
        // 토큰 검증
        queueService.validateToken(userId);
        // 예약 조회
        Reservation reservation = reservationService.getReservation(reservationId);
        // 좌석 조회
        Seat seat = seatService.findSeat(reservation.getConcertScheduleId(), reservation.getSeatId());
        // 결제
        return paymentService.payBalance(userId, seat.getPrice());
    }
}
