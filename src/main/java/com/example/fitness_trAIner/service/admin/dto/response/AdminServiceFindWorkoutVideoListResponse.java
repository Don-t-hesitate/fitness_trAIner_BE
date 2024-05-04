package com.example.fitness_trAIner.service.admin.dto.response;

import com.example.fitness_trAIner.repository.workout.WorkoutVideo;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminServiceFindWorkoutVideoListResponse {
    private List<WorkoutVideo> workoutVideoList;
}
