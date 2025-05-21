package kr.hhplus.concert.application.facade;

import kr.hhplus.concert.domain.model.concert.Concert;
import kr.hhplus.concert.domain.service.ConcertService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Import(kr.hhplus.concert.TestcontainersConfiguration.class)
@Sql("/init.sql")

class ConcertFacadeTest {

    @Autowired
    private ConcertFacade concertFacade;

    @Autowired
    private ConcertService concertService;

    @DisplayName("존재하는 콘서트 리스트에서 콘서트 아이디로 조회 할 수 있다")
    @Test
    void findConcert_In_ExistConcertList() {
        // given
        List<Concert> result = concertService.findAllConcerts();
        Long concert1Id = 1L;
        Long concert2Id = 2L;
        // when
        Concert foundConcert1 = concertService.findConcertById(concert1Id);
        Concert foundConcert2 = concertService.findConcertById(concert2Id);
        // then
        assertThat(result).hasSizeGreaterThanOrEqualTo(2);
        assertThat(result)
                .usingElementComparatorOnFields("id", "name", "venue")
                .contains(foundConcert1, foundConcert2);
        assertThat(foundConcert1.name()).isEqualTo("WORLD DJ FESTIVAL");
        assertThat(foundConcert2.name()).isEqualTo("Ultra Music Festival");
    }


}