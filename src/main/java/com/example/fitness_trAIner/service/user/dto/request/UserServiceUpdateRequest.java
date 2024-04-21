package com.example.fitness_trAIner.service.user.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserServiceUpdateRequest {
    private Long id;
    private String nickname;
    private float height;
    private float weight;
    private int age;
    private int spicyPreference;
    private Boolean meatConsumption;
    private String tastePreference;
    private int activityLevel;
    private String preferenceTypeFood;
}
