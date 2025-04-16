package kr.hhplus.concert.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class TransactionDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Request {
        private int userId;
        private Type type;
        private float amount;

        public enum Type {
            PAYMENT, CHARGE
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private int id;
        private long userId;
        private Type type;
        private float amount;
        private float balance;
        private Status status;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime processedAt;

        public enum Type {
            PAYMENT, CHARGE
        }

        public enum Status {
            SUCCESS, FAILED, PENDING
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class BalanceResponse {
        private long userId;
        private float balance;
    }
}

