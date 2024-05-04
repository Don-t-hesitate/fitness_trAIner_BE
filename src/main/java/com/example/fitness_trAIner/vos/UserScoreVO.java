package com.example.fitness_trAIner.vos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserScoreVO {
    private String nickname;
//    private String exerciseName;
    private int score;
}
