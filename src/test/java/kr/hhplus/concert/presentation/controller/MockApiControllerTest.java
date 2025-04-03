package kr.hhplus.concert.presentation.controller;

import kr.hhplus.concert.presentation.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(MockApiController.class)
public class MockApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("대기열 인입 및 토큰 발급 API 테스트")
    public void testCreateQueueToken() throws Exception {
        // Given
        QueueDTO.Request request = new QueueDTO.Request();
        request.setUserId(1L);
        String requestJson = objectMapper.writeValueAsString(request);

        // When & Then
        MvcResult result = mockMvc.perform(post("/api/queue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.enteredAt").exists())
                .andExpect(jsonPath("$.expiredAt").exists())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        QueueDTO.Response response = objectMapper.readValue(responseJson, QueueDTO.Response.class);

        assertEquals("q1w2e3r4", response.getToken());
        assertNotNull(response.getEnteredAt());
        assertNotNull(response.getExpiredAt());
    }

    @Test
    @DisplayName("토큰 조회 API 테스트")
    public void testGetToken() throws Exception {
        // Given
        Long userId = 1L;

        // When & Then
        mockMvc.perform(get("/api/queue")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("q1w2e3r4"))
                .andExpect(jsonPath("$.enteredAt").exists())
                .andExpect(jsonPath("$.expiredAt").exists());
    }

    @Test
    @DisplayName("좌석 조회 API 테스트")
    public void testGetSeat() throws Exception {
        // Given
        String token = "q1w2e3r4";
        Long concertId = 1L;
        Long scheduleId = 1L;

        // When & Then
        mockMvc.perform(get("/api/concert/{concertId}/schedule/{scheduleId}/seat", concertId, scheduleId)
                        .header("TOKEN", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatNumber").value(1))
                .andExpect(jsonPath("$.price").value(150000))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    @DisplayName("좌석 예약 API 테스트")
    public void testReserveSeat() throws Exception {
        // Given
        String token = "q1w2e3r4";
        ReservationDTO.Request request = new ReservationDTO.Request();
        request.setConcertId(1L);
        request.setScheduleId(1L);
        String requestJson = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(post("/api/reservation")
                        .header("TOKEN", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatInfo[0].seatNumber").value(1))
                .andExpect(jsonPath("$.seatInfo[0].price").value(150000))
                .andExpect(jsonPath("$.seatInfo[0].status").value("RESERVED"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.reservedAt").exists())
                .andExpect(jsonPath("$.expiredAt").exists());
    }

    @Test
    @DisplayName("잔액 조회 API 테스트")
    public void testGetBalance() throws Exception {
        // Given
        Long userId = 1L;

        // When & Then
        mockMvc.perform(get("/api/transaction/balance")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    @DisplayName("잔액 충전 API 테스트")
    public void testChargeBalance() throws Exception {
        // Given
        Long userId = 1L;
        float amount = 500F;

        // When & Then
        mockMvc.perform(post("/api/transaction/charge")
                        .param("userId", userId.toString())
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.balance").value(1500));
    }
}