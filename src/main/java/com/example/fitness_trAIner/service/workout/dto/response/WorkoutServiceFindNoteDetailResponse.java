package com.example.fitness_trAIner.service.workout.dto.response;

import com.example.fitness_trAIner.repository.workout.Workout;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutServiceFindNoteDetailResponse {
    private int totalKcal;
    private int totalPerfect;
    private int totalGood;
    private int totalBad;
    private int totalScore;
    private List<Workout> workoutList;

}
