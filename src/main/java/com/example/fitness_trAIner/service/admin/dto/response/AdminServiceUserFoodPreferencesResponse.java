package com.example.fitness_trAIner.service.admin.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminServiceUserFoodPreferencesResponse {
    private Long userId;
    private String username;
    private String nickname;
    private Integer spicyPreference;
    private Boolean meatConsumption;
    private String tastePreference;
    private Integer activityLevel;
    private String preferenceTypeFood;
}