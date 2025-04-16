package kr.hhplus.concert.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.concert.presentation.dto.*;
import kr.hhplus.concert.presentation.dto.response.QueueResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class MockApiController {

    @Tag(name = "Queue", description = "대기열 및 토큰 API")
    @Operation(summary = "대기열 인입 및 토큰 발급", description = "대기열 큐에 유저를 추가하고 토큰을 발급합니다")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{ \"type\": \"ACTIVE\", \"enteredAt\": \"yyyy-MM-dd HH:mm:ss\", \"token\": \"q1w2e3r4\", \"expiredAt\": \"yyyy-MM-dd HH:mm:ss\" }"
                    )
            )
    )
    @PostMapping("/queue")
    public ResponseEntity<QueueResponseDTO.Response> createQueueToken(@RequestBody QueueResponseDTO.Request request) {
        return ResponseEntity.ok(
                QueueResponseDTO.Response.builder()
                        .token("q1w2e3r4")
                        .enteredAt(LocalDateTime.now())
                        .expiredAt(LocalDateTime.now().plusMinutes(5)).build()
        );
    }

    @Tag(name = "Queue", description = "대기열 및 토큰 API")
    @Operation(summary = "토큰 조회", description = "유저 ID를 기반으로 토큰을 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "true")
            )
    )
    @GetMapping("/queue")
    public ResponseEntity<QueueResponseDTO.Response> getToken(@RequestParam Long userId) {
        return ResponseEntity.ok(
                QueueResponseDTO.Response.builder()
                        .token("q1w2e3r4")
                        .enteredAt(LocalDateTime.now())
                        .expiredAt(LocalDateTime.now().plusMinutes(5)).build()
        );
    }

    @Tag(name = "Schedule", description = "콘서트 스케줄 조회 API")
    @Operation(summary = "콘서트 스케줄 조회", description = "콘서트 ID를 기반으로 스케줄을 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{ \"scheduleDateTime\": \"yyyy-MM-dd\", \"startTime\": \"HH:mm\"}"
                    )
            )
    )
    @GetMapping("/schedule")
    public ResponseEntity<ScheduleDTO.Response> getSchedule(
            @RequestHeader("TOKEN") String token,
            @RequestParam Integer concertId
    ) {
        return ResponseEntity.ok(
                ScheduleDTO.Response.builder()
                        .scheduleDate(LocalTime.now())
                        .startTime(LocalTime.now())
                        .build()
        );
    }

    @Tag(name = "Reservation", description = "좌석 예약 및 조회 API")
    @Operation(summary = "좌석 조회", description = "콘서트 ID, 스케줄 ID를 기반으로 좌석을 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{ \"seatNumber\": \"1\", \"price\": \"150,000\", \"status\": \"AVAILABLE\" }"
                    )
            )
    )
    @GetMapping("/concert/{concertId}/schedule/{concertScheduleId}/seat")
    public ResponseEntity<SeatDTO.Response> getSeat(
            @RequestHeader("TOKEN") String token,
            @PathVariable Long concertId,
            @PathVariable Long scheduleId
    ) {
        return ResponseEntity.ok(
                SeatDTO.Response.builder()
                        .seatNumber(1)
                        .price(150000F)
                        .status(SeatDTO.Response.status.AVAILABLE)
                        .build()
        );
    }

    @Tag(name = "Reservation", description = "좌석 예약 및 조회 API")
    @Operation(summary = "좌석 예약", description = "콘서트 ID, 스케줄 ID를 기반으로 좌석을 예약합니다")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{ \"seatNumber\": \"1\", \"price\": \"150,000\", \"status\": \"AVAILABLE\" }"
                    )
            )
    )
    @PostMapping("/reservation")
    public ResponseEntity<ReservationDTO.Response> reserveSeat(
            @RequestHeader("TOKEN") String token,
            @RequestBody ReservationDTO.Request request
    ) {
        return ResponseEntity.ok(
                ReservationDTO.Response.builder()
                        .seatInfo(List.of(
                                SeatDTO.Response.builder()
                                        .seatNumber(1)
                                        .price(150000F)
                                        .status(SeatDTO.Response.status.RESERVED)
                                        .build()
                        ))
                        .status(ReservationDTO.Response.status.SUCCESS)
                        .reservedAt(LocalDateTime.now().plusMinutes(5))
                        .expiredAt(LocalDateTime.now().plusMinutes(10))
                        .build()
        );
    }

    @Tag(name = "Transaction", description = "잔액 조회 및 충전 API")
    @Operation(summary = "잔액 조회", description = "유저 ID를 기반으로 현재 잔액을 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{ \"userId\": \"1\", \"balance\": \"200.000\" }"
                    )
            )
    )
    @GetMapping("/transaction/balance")
    public ResponseEntity<TransactionDTO.BalanceResponse> getBalance(@RequestParam long userId) {
        float currentBalance = 1000F;
        return ResponseEntity.ok(TransactionDTO.BalanceResponse.builder()
                .userId(userId)
                .balance(currentBalance)
                .build());
    }

    @Tag(name = "Transaction", description = "잔액 조회 및 충전 API")
    @Operation(summary = "잔액 충전", description = "유저 ID를 기반으로 특정 금액을 충전합니다")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{ \"userId\": \"1\", \"balance\": \"400,000\" }"
                    )
            )
    )
    @PostMapping("/transaction/charge")
    public ResponseEntity<TransactionDTO.BalanceResponse> chargeBalance(
            @RequestParam long userId,
            @RequestParam float amount
    ) {
        float currentBalance = 1000F;
        float newBalance = currentBalance + amount;

        return ResponseEntity.ok(TransactionDTO.BalanceResponse.builder()
                .userId(userId)
                .balance(newBalance)
                .build());
    }

}
