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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class SeatServiceTest {

//    @Mock
//    private SeatRepository seatRepository;
//
//    @InjectMocks
//    private  SeatService seatService;
//
//    @DisplayName("예약이 불가능한 좌석이면 좌석을 반환한다")
//    @Test
//    void findSeat_success_whenAvailable() {
//        // given
//        Long id = 1L;
//        Long concertScheduleId = 1L;
//        Long seatNumber = 1L;
//        Float price = 10000F;
//        Seat seat = new Seat(id, seatNumber, price, SeatStatus.RESERVED);
//        seat.reserve();
//        Mockito.when(seatRepository.findById(concertScheduleId)).thenReturn(Optional.of(seat));
//        // when
//        // then
//        assertThatThrownBy(() -> seatService.findSeat(concertScheduleId))
//                .isInstanceOf(SeatUnavailableException.class)
//                .hasMessageContaining("예약이 불가능한 좌석입니다. 좌석상태 : " + SeatStatus.RESERVED);
//    }


}