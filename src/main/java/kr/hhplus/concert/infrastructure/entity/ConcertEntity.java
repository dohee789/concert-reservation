package kr.hhplus.concert.infrastructure.entity;

import jakarta.persistence.*;
import kr.hhplus.concert.domain.model.Concert;
import lombok.*;

@Entity
@Table(name = "concert")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class ConcertEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String venue;

    public static ConcertEntity from(Concert concert) {
        return ConcertEntity.builder()
                .id(concert.id())
                .name(concert.name())
                .venue(concert.venue())
                .build();
    }

    public Concert of() {
        return Concert.builder()
                .id(this.id)
                .name(this.name)
                .venue(this.venue)
                .build();
    }
}
