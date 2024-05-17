package com.example.fitness_trAIner.service.user.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserServiceSignupRequest {
    private String username;
    private String password;
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
