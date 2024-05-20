package com.example.fitness_trAIner.vos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DietVO {
    private Long userId;
    private String dietDate;
    private String foodId;
    private Double totalCalories;
}
