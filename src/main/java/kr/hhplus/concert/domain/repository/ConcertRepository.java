package kr.hhplus.concert.domain.repository;

import kr.hhplus.concert.domain.model.concert.Concert;

import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    Optional<Concert> findById(Long id);

    Concert save(Concert concert);

    List<Concert> findAllConcerts();
}
