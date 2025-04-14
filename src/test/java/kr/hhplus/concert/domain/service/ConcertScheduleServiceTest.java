package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.ConcertSchedule;
import kr.hhplus.concert.domain.repository.ConcertScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
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
        LocalDate inValidScheduleDate = LocalDate.of(2029, 4, 15);// 존재하지 않는 날짜
        LocalDate ValidScheduleDate = LocalDate.of(2025, 4, 15);

        ConcertSchedule schedule = new ConcertSchedule(
                1, concertId, ValidScheduleDate, LocalTime.now()
        );

        Mockito.when(concertScheduleRepository.findConcertSchedulesById(concertId)).thenReturn(List.of(schedule));

        // when & then
        assertThatThrownBy(() -> concertScheduleService.findConcertSchedulesByIdAndDate(concertId, inValidScheduleDate))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 날짜의 콘서트 스케줄이 없습니다.");
    }

}