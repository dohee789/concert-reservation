package kr.hhplus.concert.presentation.dto;

import lombok.Builder;
import lombok.Getter;

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
