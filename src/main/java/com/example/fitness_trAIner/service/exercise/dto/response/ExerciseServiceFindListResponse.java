package com.example.fitness_trAIner.service.exercise.dto.response;

import com.example.fitness_trAIner.repository.exercise.Exercise;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExerciseServiceFindListResponse {
    private List<Exercise> exerciseList;

}
