package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.domain.model.seat.SeatStatus;
import kr.hhplus.concert.infrastructure.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeatJpaRepository extends JpaRepository<SeatEntity, Long> {
    @Query("SELECT COUNT(s) FROM SeatEntity s WHERE s.concertSchedule.id = :scheduleId AND s.seatStatus = :seatStatus")
    Integer countByConcertScheduleIdAndSeatStatus(@Param("scheduleId") Long scheduleId,
                                            @Param("status") SeatStatus seatStatus);
}
