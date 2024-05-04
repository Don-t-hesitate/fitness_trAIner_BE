package com.example.fitness_trAIner.service.user.dto.response;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserServiceDetailInfoResponse {
    private String username;
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
}
