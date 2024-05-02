package com.example.fitness_trAIner.service.workout;

import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceFindNoteListRequest;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceSaveVideoRequest;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceSaveWorkoutRequest;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceVideoStreamRequest;
import com.example.fitness_trAIner.service.workout.dto.response.WorkoutServiceFindNoteDetailResponse;
import com.example.fitness_trAIner.service.workout.dto.response.WorkoutServiceFindNoteListResponse;
import com.example.fitness_trAIner.service.workout.dto.response.WorkoutServiceFindVideoListResponse;
import com.example.fitness_trAIner.service.workout.dto.response.WorkoutServiceSaveNoteResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface WorkoutService {
    public String fileUpload(MultipartFile file, WorkoutServiceSaveVideoRequest request);
    public WorkoutServiceSaveNoteResponse saveNote(Long userId);
    public WorkoutServiceFindNoteListResponse findNoteList(WorkoutServiceFindNoteListRequest request);
    public String saveWorkout(WorkoutServiceSaveWorkoutRequest request);
    public void workoutVideoStream(WorkoutServiceVideoStreamRequest request) throws IOException;
    public WorkoutServiceFindVideoListResponse findWorkoutVideoListByNoteId(Long noteId);
    public WorkoutServiceFindNoteDetailResponse findNoteDetail(Long noteId);

}
