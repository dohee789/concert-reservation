package kr.hhplus.concert.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

public class SeatDTO {
    @Getter
    @Builder
    public static class Response {
        private int seatNumber;
        private float price;
        private status status;

        public enum status {
            AVAILABLE, RESERVED
        };
    }
}
