package com.example.fitness_trAIner.service.exercise.dto.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExerciseServiceVideoStreamRequest {
    private String exerciseName;
    HttpServletRequest request;
    HttpServletResponse response;
}
