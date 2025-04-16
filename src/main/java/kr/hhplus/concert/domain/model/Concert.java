package kr.hhplus.concert.domain.model;

import lombok.Builder;

@Builder
public record Concert (
     Long id,
     String name,
     String venue
){}
