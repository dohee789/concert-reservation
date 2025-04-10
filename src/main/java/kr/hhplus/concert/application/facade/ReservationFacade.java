package kr.hhplus.concert.application.facade;

import kr.hhplus.concert.domain.model.Concert;
import kr.hhplus.concert.domain.model.Reservation;
import kr.hhplus.concert.domain.model.Seat;
import kr.hhplus.concert.domain.service.QueueService;
import kr.hhplus.concert.domain.service.ReservationService;
import kr.hhplus.concert.domain.service.SeatService;
import kr.hhplus.concert.presentation.dto.request.ReservationRequestDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationFacade {
    private QueueService queueService;
    private SeatService seatService;
    private ReservationService reservationService;

    public Reservation makeReservation(ReservationRequestDTO dto) {
        // 토큰 검증
        queueService.validateToken(dto.getUserId());
        // 콘서트 좌석 찾기
        Seat seat = seatService.findSeat(dto.getConcertScheduleId(), dto.getSeatId());
        // 좌석 점유
        seatService.assignSeat(seat.getConcertScheduleId(), dto.getSeatId());
        // 예약 정보 저장
        return reservationService.makeReservation(dto.getUserId(), dto.getConcertScheduleId(), dto.getSeatId());
    }
}
