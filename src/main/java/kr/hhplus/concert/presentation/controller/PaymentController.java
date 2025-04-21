package kr.hhplus.concert.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.concert.application.facade.PaymentFacade;
import kr.hhplus.concert.domain.model.Wallet;
import kr.hhplus.concert.presentation.dto.PaymentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentFacade paymentFacade;

    @Tag(name = "Payment", description = "결제 API")
    @Operation(summary = "잔액 충전", description = "콘서트를 결제합니다")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{ \"userId\": \"1\", \"balance\": \"400,000\" }"
                    )
            )
    )

    @PostMapping
    public ResponseEntity<Wallet> payment(
            @RequestHeader("Token") String token,
            @RequestBody @Valid PaymentDTO.PaymentRequest request
    ) {
        Wallet payment = paymentFacade.payment(request.userId(), request.amount());
        return ResponseEntity.ok(payment);
    }
}
