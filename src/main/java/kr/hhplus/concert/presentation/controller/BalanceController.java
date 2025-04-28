package kr.hhplus.concert.presentation.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.concert.domain.model.Wallet;
import kr.hhplus.concert.domain.service.WalletService;
import kr.hhplus.concert.presentation.dto.WalletDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/balance")
@RequiredArgsConstructor
public class BalanceController {

    private final WalletService paymentService;

    @Tag(name = "Transaction", description = "금액 충전 API")
    @Operation(summary = "잔액 충전", description = "유저 ID를 기반으로 금액을 충전합니다")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{ \"userId\": \"1\", \"balance\": \"400,000\" }"
                    )
            )
    )
    @PostMapping("/charge")
    public ResponseEntity<Wallet> chargeBalance(
            @RequestHeader("Token") String token,
            @RequestBody @Valid WalletDTO.PaymentRequest request
    ) {
        Wallet charge = paymentService.chargeBalance(request.userId(), request.balance());
        return ResponseEntity.ok(charge);
    }

    @Tag(name = "Transaction", description = "잔액 조회 API")
    @Operation(summary = "잔액 조회", description = "유저 ID를 기반으로 잔액을 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{ \"userId\": \"1\", \"balance\": \"400,000\" }"
                    )
            )
    )
    @GetMapping
    public ResponseEntity<Wallet> getBalance(
            @PathVariable Long userId
    ) {
        Wallet payment = paymentService.getBalance(userId);
        return ResponseEntity.ok((payment));
    }
}
