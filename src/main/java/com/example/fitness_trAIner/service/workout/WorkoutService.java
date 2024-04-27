package com.example.fitness_trAIner.service.workout;

import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceSaveVideoRequest;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceSaveWorkoutRequest;
import com.example.fitness_trAIner.service.workout.dto.response.WorkoutServiceSaveNoteResponse;
import org.springframework.web.multipart.MultipartFile;

public interface WorkoutService {
    public String fileUpload(MultipartFile file, WorkoutServiceSaveVideoRequest request);
    public WorkoutServiceSaveNoteResponse saveNote(Long id);
    public String saveWorkout(WorkoutServiceSaveWorkoutRequest request);

}
