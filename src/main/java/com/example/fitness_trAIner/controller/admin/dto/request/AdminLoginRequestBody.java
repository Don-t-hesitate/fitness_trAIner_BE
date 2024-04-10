package com.example.fitness_trAIner.controller.admin.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminLoginRequestBody {
    private String username;
    private String password;
}
