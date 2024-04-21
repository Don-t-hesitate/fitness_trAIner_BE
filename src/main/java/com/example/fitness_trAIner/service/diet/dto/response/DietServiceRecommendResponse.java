package com.example.fitness_trAIner.service.diet.dto.response;

import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DietServiceRecommendResponse {
    private List<String> foodRecommend;
    private int totalCalorie;
}