package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.Concert;
import kr.hhplus.concert.domain.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;

    public Concert findConcertById(Long id) {
        return concertRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("콘서트 정보를 찾을 수 없습니다."));
    }

}
