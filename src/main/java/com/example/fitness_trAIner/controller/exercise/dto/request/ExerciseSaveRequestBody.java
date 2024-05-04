package com.example.fitness_trAIner.controller.exercise.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExerciseSaveRequestBody {
    private String exerciseName;
    private String exerciseType;
    private int perKcal;
}
