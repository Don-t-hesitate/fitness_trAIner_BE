package com.example.fitness_trAIner.service.diet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class DietServiceInitialFoodList {
    private String foodName;
    private String taste;
    private String mainIngredient;
    private String secondaryIngredient;
    private String cookMethod;


    public DietServiceInitialFoodList() {

    }
}
