package com.example.fitness_trAIner.service.diet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DietServiceUserFoodInfo {
    private final Long userId;
    private Float height;
    private Float weight;
    private Integer age;
    private String gender;
    private Integer spicyPreference;
    private Boolean meatConsumption;
    private String tastePreference;
    private Integer activityLevel;
    private String preferenceTypeFood;
    private String preferenceFoods;

    public DietServiceUserFoodInfo(Long userId, Float height, Float weight, Integer age, String gender, Integer spicyPreference, Boolean meatConsumption, String tastePreference, Integer activityLevel, String preferenceTypeFood, String preferenceFoods) {
        this.userId = userId;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
        this.spicyPreference = spicyPreference;
        this.meatConsumption = meatConsumption;
        this.tastePreference = tastePreference;
        this.activityLevel = activityLevel;
        this.preferenceTypeFood = preferenceTypeFood;
        this.preferenceFoods = preferenceFoods;
    }
}
