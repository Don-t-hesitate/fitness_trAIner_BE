package com.example.fitness_trAIner.service.exercise.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExerciseServiceUpdateRequest {
    private String previousName;
    private String exerciseName;
    private String exerciseType;
    private int perKcal;
}
