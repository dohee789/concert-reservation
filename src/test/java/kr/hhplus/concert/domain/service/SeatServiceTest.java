package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.exception.SeatUnavailableException;
import kr.hhplus.concert.domain.model.Seat;
import kr.hhplus.concert.domain.model.enums.SeatStatus;
import kr.hhplus.concert.domain.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private  SeatService seatService;

    @DisplayName("예약이 불가능한 좌석이면 좌석을 반환한다")
    @Test
    void findSeat_success_whenAvailable() {
        // given
        Integer id = 1;
        Integer concertScheduleId = 1;
        Integer seatNumber = 1;
        Float price = 10000F;
        Seat seat = new Seat(id, concertScheduleId, seatNumber, price, SeatStatus.RESERVED);
        seat.isReserved();
        Mockito.when(seatRepository.findSeatById(concertScheduleId, seatNumber)).thenReturn(seat);
        // when
        // then
        assertThatThrownBy(() -> seatService.findSeat(concertScheduleId, seatNumber))
                .isInstanceOf(SeatUnavailableException.class)
                .hasMessageContaining("예약이 불가능한 좌석입니다. 좌석상태 : " + SeatStatus.RESERVED);
    }


}