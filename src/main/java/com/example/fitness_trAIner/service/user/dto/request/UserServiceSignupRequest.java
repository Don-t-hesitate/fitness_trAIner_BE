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
    private int height;
    private int weight;
    private int age;
    private int spicyPreference;
    private Boolean meatConsumption;
    private String tastePreference;
    private int activityLevel;
    private String preferenceTypeFood;

}
