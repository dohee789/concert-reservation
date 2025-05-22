package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.concert.Concert;
import kr.hhplus.concert.domain.model.concert.ConcertSchedule;
import kr.hhplus.concert.domain.repository.ConcertScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class ConcertScheduleServiceTest {
    @Mock
    private ConcertScheduleRepository concertScheduleRepository;

    @InjectMocks
    private ConcertScheduleService concertScheduleService;

    @Test
    @DisplayName("해당 날짜의 콘서트 스케줄이 없으면 예외가 발생한다.")
    void findConcertSchedulesByIdAndDate_notFound() {
        // given
        Long concertId = 1L;
        LocalDateTime inValidScheduleDate = LocalDateTime.of(2029, 4, 15, 10, 10, 10);// 존재하지 않는 날짜
        LocalDateTime ValidScheduleDate = LocalDateTime.of(2025, 4, 15, 10, 10, 10);

        LocalDateTime openDate = LocalDateTime.of(2025, 4, 15, 10, 10, 10);

        ConcertSchedule schedule = new ConcertSchedule(
                1L, ValidScheduleDate, openDate, Concert.builder().build()
        );

        Mockito.when(concertScheduleRepository.findConcertSchedulesById(concertId)).thenReturn(List.of(schedule));

        // when & then
        assertThatThrownBy(() -> concertScheduleService.findConcertSchedulesByIdAndDate(concertId, inValidScheduleDate))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 날짜의 콘서트 스케줄이 없습니다.");
    }

}