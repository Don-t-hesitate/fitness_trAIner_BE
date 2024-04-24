package com.example.fitness_trAIner.controller.user.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFindUsernameRequestBody {
    private String nickname;
    private Integer age;
}
