package kr.hhplus.concert.application.facade;

import kr.hhplus.concert.application.lock.DistributeLock;
import kr.hhplus.concert.domain.model.Wallet;
import kr.hhplus.concert.domain.model.Reservation;
import kr.hhplus.concert.domain.model.Seat;
import kr.hhplus.concert.domain.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletFacade {

    private final WalletHistoryService walletHistoryService;
    private final WalletService walletService;
    private final ReservationService reservationService;
    private final QueueService queueService;
    private final SeatService seatService;
    private final ConcertRankingService concertRankingService;

    @DistributeLock(key = "'pay-userId:' + #userId", waitTimeSec = 3, leaseTimeSec = 6)
    @Transactional
    public Wallet wallet(Long userId, Long reservationId) {
        // 토큰 및 대기열 검증
        queueService.getValidatedQueue(userId);
        // 예약 조회
        Reservation reservation = reservationService.getReservation(reservationId);
        // 좌석 조회
        Seat seat = seatService.findSeat(reservation.getSeat().getConcertSchedule().id());
        // 결제
        Wallet wallet = walletService.payMoney(reservation, seat.getPrice());
        // 결제 내역 저장
        walletHistoryService.save(wallet);
        // 매진 시점 랭킹 기록
        if (seatService.isSoldOut(reservation.getSeat().getConcertSchedule().id())) {
            concertRankingService.tryRecordSoldOut(reservation, wallet.getProcessedAt());
        }
        return wallet;
    }

}
