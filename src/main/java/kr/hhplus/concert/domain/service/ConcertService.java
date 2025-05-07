package kr.hhplus.concert.domain.service;

import kr.hhplus.concert.domain.model.Concert;
import kr.hhplus.concert.domain.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;

    @Cacheable(value = "concert", key = "#id", unless = "#result == null")
    @Transactional(readOnly = true)
    public Concert findConcertById(Long id) {
        return concertRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("콘서트 정보를 찾을 수 없습니다."));
    }

    public List<Concert> findAllConcerts() {
        return concertRepository.findAllConcerts();
    }

}
