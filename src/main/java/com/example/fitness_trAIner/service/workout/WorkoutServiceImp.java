package com.example.fitness_trAIner.service.workout;

import com.example.fitness_trAIner.common.exception.exceptions.FileStoreException;
import com.example.fitness_trAIner.repository.workout.*;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceSaveVideoRequest;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceSaveWorkoutRequest;
import com.example.fitness_trAIner.service.workout.dto.response.WorkoutServiceSaveNoteResponse;
import com.example.fitness_trAIner.vos.WorkoutVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class WorkoutServiceImp implements WorkoutService {
    private final NoteRepository noteRepository;
    private final WorkoutRepository workoutRepository;
    private final WorkoutVideoRepository workoutVideoRepository;
    @Value("${videopath.user}")
    private String uploadDir;
//    private String uploadDir = "C:\\video";

//    private String uploadDir = "/home/t24108/ai/video/users";
    @Override
    public String fileUpload(MultipartFile file, WorkoutServiceSaveVideoRequest request) {
        if (!file.getContentType().equals("video/mp4")) {
            log.error("파일 확장자 에러");
            throw new FileStoreException("파일 확장자가 알맞지 않습니다.");
        }

        String randomName = UUID.randomUUID().toString();
        Path copyOfLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(randomName+".mp4"));
        File uploadPath = copyOfLocation.toFile();
        uploadPath.mkdirs();

        WorkoutVideo workoutVideo;

        try {
            System.out.println("file경로 " + copyOfLocation.toString());
            Files.copy(file.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
            workoutVideo = workoutVideoRepository.save(WorkoutVideo.builder()
                    .fileName(copyOfLocation.toString())
                    .noteId(request.getNoteId())
                    .exerciseName(request.getExerciseName())
                    .build());

            return "파일 저장 성공";
        }catch (IOException e) {
            e.printStackTrace();
            throw new FileStoreException("파일 저장 실패 : " + file.getOriginalFilename());
        }

    }

    @Override
    public WorkoutServiceSaveNoteResponse saveNote(Long id) {
        Note note = noteRepository.save(Note.builder().userId(id).build());

        return WorkoutServiceSaveNoteResponse.builder()
                .noteId(note.getNoteId())
                .build();
    }

    @Override
    public String saveWorkout(WorkoutServiceSaveWorkoutRequest request) {

        for (WorkoutVO workoutVO : request.getWorkoutList()) {
            Workout workout = new Workout();
            workout.setNoteId(request.getNoteId());
            workout.setExerciseName(workoutVO.getExerciseName());
            workout.setSetNum(workoutVO.getSetNum());
            workout.setRepeats(workoutVO.getRepeats());
            workoutRepository.save(workout);
        }

        return "운동 내용 업로드 성공";
    }


}
