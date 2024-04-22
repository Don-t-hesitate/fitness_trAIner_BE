package com.example.fitness_trAIner.controller.user.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFindUsernameRequestBody {
    private String nickname;
    private Integer age;
}
