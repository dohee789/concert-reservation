package kr.hhplus.concert.application.facade;

import kr.hhplus.concert.application.dto.ReservationCommand;
import kr.hhplus.concert.application.dto.ReservationResult;
import kr.hhplus.concert.domain.model.Queue;
import kr.hhplus.concert.domain.model.ConcertSchedule;
import kr.hhplus.concert.domain.model.Reservation;
import kr.hhplus.concert.domain.model.Seat;
import kr.hhplus.concert.domain.service.ConcertScheduleService;
import kr.hhplus.concert.domain.service.QueueService;
import kr.hhplus.concert.domain.service.ReservationService;
import kr.hhplus.concert.domain.service.SeatService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ReservationFacade {
    private QueueService queueService;
    private SeatService seatService;
    private ReservationService reservationService;

    public ReservationResult makeReservation(ReservationCommand command) {
        // 토큰 검증
        Queue queue = queueService.getValidatedQueue(command.userId());
        // 콘서트 좌석 찾기
        Seat seat = seatService.findSeat(command.concertScheduleId(), command.seatId());
        // 좌석 점유
        seatService.assignSeat(seat.getConcertSchedule().id(), command.seatId());
        // 예약 정보 저장
        Reservation reservation = reservationService.makeReservation(queue, seat);

        return ReservationResult.from(reservation);
    }
}
