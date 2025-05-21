package kr.hhplus.concert.infrastructure.repository;

import kr.hhplus.concert.domain.model.concert.Concert;
import kr.hhplus.concert.domain.repository.ConcertRepository;
import kr.hhplus.concert.infrastructure.entity.ConcertEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;

    @Override
    public Optional<Concert> findById(Long userId) {
        return concertJpaRepository.findById(userId)
                .map(ConcertEntity::of);
    }

    @Override
    @Transactional
    public Concert save(Concert concert) {
        ConcertEntity concertEntity = concertJpaRepository.save(ConcertEntity.from(concert));
        return concertEntity.of();
    }

    @Override
    public List<Concert> findAllConcerts() {
        return concertJpaRepository.findAll().stream()
                .map(ConcertEntity::of)
                .toList();
    }

}
