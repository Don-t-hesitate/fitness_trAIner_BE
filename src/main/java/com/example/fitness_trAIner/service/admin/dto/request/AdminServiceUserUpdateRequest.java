package com.example.fitness_trAIner.service.admin.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminServiceUserUpdateRequest {
    private Long userId;
    private String nickname;
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
}
