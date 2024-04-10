package com.example.fitness_trAIner.controller.user.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class UserUpdateRequestBody {
    private Long id;
    private String nickname;
    private Integer height;
    private Integer weight;
    private Integer age;
    private Integer spicyPreference;
    private Boolean meatConsumption;
    private String tastePreference;
    private Integer activityLevel;
    private String preferenceTypeFood;
}
