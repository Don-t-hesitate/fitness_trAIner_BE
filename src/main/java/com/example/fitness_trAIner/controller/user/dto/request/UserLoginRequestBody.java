package com.example.fitness_trAIner.controller.user.dto.request;

import lombok.*;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginRequestBody {
    private String username;
    private String password;
}
