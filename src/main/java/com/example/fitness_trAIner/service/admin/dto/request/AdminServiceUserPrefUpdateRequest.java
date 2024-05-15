package com.example.fitness_trAIner.service.admin.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminServiceUserPrefUpdateRequest {
    private Long userId;
    private Integer spicyPreference;
    private Boolean meatConsumption;
    private String tastePreference;
    private Integer activityLevel;
    private String preferenceTypeFood;
}
