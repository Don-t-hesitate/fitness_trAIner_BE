package com.example.fitness_trAIner.service.exercise;

import com.example.fitness_trAIner.service.exercise.dto.request.ExerciseServiceSaveRequest;
import com.example.fitness_trAIner.service.exercise.dto.request.ExerciseServiceUpdateRequest;
import com.example.fitness_trAIner.service.exercise.dto.request.ExerciseServiceVideoStreamRequest;
import com.example.fitness_trAIner.service.exercise.dto.response.ExerciseServiceFindListResponse;
import com.example.fitness_trAIner.service.exercise.dto.response.ExerciseServiceSaveResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ExerciseService {
    public ExerciseServiceSaveResponse saveExercise(ExerciseServiceSaveRequest request);

    public ExerciseServiceFindListResponse findAllExercise();
    public String updateExercise(ExerciseServiceUpdateRequest request);
    public String deleteExercise(String exerciseName);
    public void exerciseVideoStream(ExerciseServiceVideoStreamRequest request) throws IOException;
    public String exerciseVideoUpload(MultipartFile file, String exerciseName);
    public String deleteExerciseVideo(String exerciseName);

}
