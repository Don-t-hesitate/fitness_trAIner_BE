package com.example.fitness_trAIner.vos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class WorkoutVO {
    private String exerciseName;
    private int setNum;
    private int repeats;
}
