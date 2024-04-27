package com.example.fitness_trAIner.service.workout.dto.request;

import com.example.fitness_trAIner.vos.WorkoutVO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutServiceSaveWorkoutRequest {
    private Long noteId;
    private Integer scoreCool;
    private Integer scoreGood;
    private Integer scoreNotgood;
    private List<WorkoutVO> workoutList;
}
