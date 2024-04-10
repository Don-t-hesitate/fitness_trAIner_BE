package com.example.fitness_trAIner.service.user.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserServiceChangePasswordRequest {
    private String nickname;
    private String username;
    private String newPassword;
}
