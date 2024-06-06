package com.example.fitness_trAIner.controller.diet.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class DietRecommendRequestBody {
    private Long userId;
    private List<String> consumedFoodNames;

}