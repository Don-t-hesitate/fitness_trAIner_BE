package com.example.fitness_trAIner.controller.workout.dto.request;

import com.example.fitness_trAIner.vos.WorkoutVO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutSaveWokroutRequestBody {
    private Long userId;
    private Long noteId;
    private String exerciseName;
    private List<WorkoutVO> workoutList;
}
