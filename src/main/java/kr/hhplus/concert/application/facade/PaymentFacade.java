package kr.hhplus.concert.application.facade;

import kr.hhplus.concert.domain.model.Wallet;
import kr.hhplus.concert.domain.model.Reservation;
import kr.hhplus.concert.domain.model.Seat;
import kr.hhplus.concert.domain.service.WalletService;
import kr.hhplus.concert.domain.service.QueueService;
import kr.hhplus.concert.domain.service.ReservationService;
import kr.hhplus.concert.domain.service.SeatService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentFacade {

    private final WalletService paymentService;
    private final ReservationService reservationService;
    private final QueueService queueService;
    private final SeatService seatService;

    public Wallet payment(Long userId, Float amount) {
        // 토큰 및 대기열 검증
        queueService.getValidatedQueue(userId);
        // 예약 조회
        Reservation reservation = reservationService.getReservation(userId);
        // 좌석 조회
        Seat seat = seatService.findSeat(reservation.getSeat().getConcertSchedule().id(), reservation.getSeat().getSeatNumber());
        // 결제
        return paymentService.payBalance(userId, seat.getPrice());
    }

}
