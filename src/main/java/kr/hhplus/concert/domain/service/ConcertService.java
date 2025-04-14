package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.Concert;
import kr.hhplus.concert.domain.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;

    public Concert findConcertById(Long id) {
        return concertRepository.findById(id);
    }

}
