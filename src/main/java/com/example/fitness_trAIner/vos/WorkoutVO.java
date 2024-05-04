package com.example.fitness_trAIner.vos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class WorkoutVO {
    private int setNum;
    private int repeats;
    private int weight;
    private int scorePerfect;
    private int scoreGood;
    private int scoreBad;
}
