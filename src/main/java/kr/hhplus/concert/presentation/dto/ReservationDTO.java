package kr.hhplus.concert.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationDTO {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long concertId;
        private Long scheduleId;
    }

    @Getter
    @Builder
    public static class Response {
        public List<SeatDTO.Response> seatInfo;
        private status status;

        public enum status {
            SUCCESS, FAILED
        };
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime reservedAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime expiredAt;
    }
}
