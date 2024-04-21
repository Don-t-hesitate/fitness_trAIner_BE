package com.example.fitness_trAIner.service.diet.dto.request;

import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DietServiceRecommendRequest {
    private String category;
}