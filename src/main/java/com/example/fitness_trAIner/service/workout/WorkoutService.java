package com.example.fitness_trAIner.service.workout;

import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceSaveWorkoutRequest;
import org.springframework.web.multipart.MultipartFile;

public interface WorkoutService {
    public void fileUpload(MultipartFile file);
    public String saveNote(Long id);
    public String saveWorkout(WorkoutServiceSaveWorkoutRequest request);

}
