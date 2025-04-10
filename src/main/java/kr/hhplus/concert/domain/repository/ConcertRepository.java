package kr.hhplus.concert.domain.repository;

import kr.hhplus.concert.domain.model.Concert;

public interface ConcertRepository {
    Concert findById(Long id);

}
