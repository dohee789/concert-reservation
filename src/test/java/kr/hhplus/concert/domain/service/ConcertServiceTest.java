package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.Concert;
import kr.hhplus.concert.domain.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class ConcertServiceTest {
    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    @DisplayName("콘서트 아이디로 콘서트를 조회 할 수 있다")
    @Test
    void findConcertByConcertId() {
        // given
        Long concertId = 1L;
        Concert concert = new Concert(concertId, "콘서트입니다", "콘서트라구요");
        // when
        Mockito.when(concertRepository.findById(concertId)).thenReturn(Optional.of(concert));
        Concert result = concertService.findConcertById(concertId);
        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(concertId);
        assertThat(result.name()).isEqualTo("콘서트입니다");
        assertThat(result.venue()).isEqualTo("콘서트라구요");
    }

}