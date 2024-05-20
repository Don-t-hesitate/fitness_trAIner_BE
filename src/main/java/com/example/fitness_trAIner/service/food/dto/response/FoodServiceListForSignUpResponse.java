package com.example.fitness_trAIner.service.food.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class FoodServiceListForSignUpResponse {
    private String foodName;
    private String taste;
    private String mainIngredient;
    private String secondaryIngredient;
    private String cookMethod;

    public FoodServiceListForSignUpResponse() {

    }
}
