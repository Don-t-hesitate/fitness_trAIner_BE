package com.example.fitness_trAIner.service.diet.dto.response;

import com.example.fitness_trAIner.service.diet.dto.DietServiceInitialFoodList;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DietServiceRecommendResponse {
    private List<Map<String, Object>> foodRecommend;
}