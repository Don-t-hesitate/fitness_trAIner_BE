package com.example.fitness_trAIner.service.workout.dto.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class WorkoutServiceVideoStreamRequest {
    private Long workoutVideoId;
    HttpServletRequest request;
    HttpServletResponse response;
}
