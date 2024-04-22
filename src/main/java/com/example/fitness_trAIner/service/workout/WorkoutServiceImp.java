package com.example.fitness_trAIner.service.workout;

import com.example.fitness_trAIner.common.exception.exceptions.FileStoreException;
import com.example.fitness_trAIner.repository.workout.Note;
import com.example.fitness_trAIner.repository.workout.NoteRepository;
import com.example.fitness_trAIner.repository.workout.Workout;
import com.example.fitness_trAIner.repository.workout.WorkoutRepository;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceSaveWorkoutRequest;
import com.example.fitness_trAIner.vos.WorkoutVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private String uploadDir = "C:\\video";

//    private String uploadDir = "/home/t24108/ai/video/users";
    @Override
    public void fileUpload(MultipartFile file) {
        if (!file.getContentType().equals("video/mp4")) {
            log.error("파일 확장자 에러");
            throw new FileStoreException("파일 확장자가 알맞지 않습니다.");
        }

        String randomName = UUID.randomUUID().toString();
        Path copyOfLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(randomName+".mp4"));
        File uploadPath = copyOfLocation.toFile();
        uploadPath.mkdirs();

        try{
            System.out.println("file경로 " + copyOfLocation.toString());
            Files.copy(file.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);

        }catch (IOException e) {
            e.printStackTrace();
            throw new FileStoreException("파일 저장 실패 : " + file.getOriginalFilename());
        }

    }

    @Override
    public String saveNote(Long id) {
        Note note = noteRepository.save(Note.builder().userId(id).build());
        return "운동 일지 생성 성공";
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
