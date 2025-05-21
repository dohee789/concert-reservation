package kr.hhplus.concert.domain.model.concert;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@Builder
public record Concert (
     Long id,
     String name,
     String venue
){}
