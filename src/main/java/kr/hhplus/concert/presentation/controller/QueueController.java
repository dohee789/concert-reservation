package kr.hhplus.concert.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.concert.application.facade.QueueFacade;
import kr.hhplus.concert.domain.model.queue.Queue;
import kr.hhplus.concert.presentation.dto.QueueDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class QueueController {

    private QueueFacade queueFacade;

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
    @PostMapping("/create")
    public ResponseEntity<QueueDTO.QueueResponse> createToken(
            @RequestBody QueueDTO.QueueRequest request
    ) {
        Queue token = queueFacade.registerToken(request.userId(), request.concertScheduleId());
        return ResponseEntity.ok(QueueDTO.QueueResponse.of(token));
    }

    @Tag(name = "Queue", description = "대기열 및 토큰 API")
    @Operation(summary = "토큰 조회", description = "유저 ID를 기반으로 토큰과 대기열을 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "true")
            )
    )
    @GetMapping("/get/{userId}")
    public ResponseEntity<QueueDTO.QueueResponse> getToken(
            @PathVariable Long userId
    ) {
        Queue token = queueFacade.getValidatedQueue(userId);
        return ResponseEntity.ok(QueueDTO.QueueResponse.of(token));
    }


}
