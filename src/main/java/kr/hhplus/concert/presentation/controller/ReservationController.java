package kr.hhplus.concert.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.concert.application.dto.ReservationCommand;
import kr.hhplus.concert.application.dto.ReservationResult;
import kr.hhplus.concert.application.facade.ReservationFacade;
import kr.hhplus.concert.presentation.dto.ReservationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationFacade reservationFacade;

    @Tag(name = "Reservation", description = "콘서트 예약 API")
    @Operation(summary = "콘서트 예약", description = "콘서트를 예약합니다")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{ \"userId\": \"1\", \"concertName\": \"NCT Dream\", \"concertScheduleDate\": \"2025-05-15\"" +
                                    ", \"seatNumber\": \"1\", \"reservedAt\": \"2025-04-15\" }"
                    )
            )
    )

    @PostMapping
    public ResponseEntity<ReservationDTO.ReservationResponse> reserve(
            @RequestHeader("Token") String token,
            @RequestBody @Valid ReservationDTO.ReservationRequest request
    ) {
        ReservationCommand command = request.toCommand(token); // DTO → Command 변환
        ReservationResult reservation = reservationFacade.makeReservation(command);
        return ResponseEntity.ok(ReservationDTO.ReservationResponse.of(reservation)); // 도메인 객체 → 응답 DTO
    }

}
