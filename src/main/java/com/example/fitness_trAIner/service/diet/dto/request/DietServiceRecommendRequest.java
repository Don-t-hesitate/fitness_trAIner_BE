package com.example.fitness_trAIner.service.diet.dto.request;

import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DietServiceRecommendRequest {
    private Long userId;
    private List<String> consumedFoodNames;
}