package com.example.fitness_trAIner.controller.exercise.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExerciseUpdateRequestBody {
    private String exerciseName;
    private String newExerciseName;
    private String newExerciseType;
    private int newPerKcal;
}
