package com.example.fitness_trAIner.controller.workout.dto.request;

import com.example.fitness_trAIner.vos.WorkoutVO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutSaveWokroutRequestBody {
    private Long noteId;
    private int scoreCool;
    private int scoreGood;
    private int scoreNotgood;
    private List<WorkoutVO> workoutList;
}
