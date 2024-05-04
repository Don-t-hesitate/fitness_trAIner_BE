package com.example.fitness_trAIner.service.workout.dto.response;

import com.example.fitness_trAIner.repository.workout.WorkoutVideo;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutServiceFindVideoListResponse {
    private List<WorkoutVideo> workoutVideoList;
}
