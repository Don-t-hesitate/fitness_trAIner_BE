package com.example.fitness_trAIner.service.admin.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminServiceLoginRequest {
    private String username;
    private String password;
}
