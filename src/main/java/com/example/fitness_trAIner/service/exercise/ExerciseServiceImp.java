package com.example.fitness_trAIner.service.exercise;

import com.example.fitness_trAIner.common.exception.exceptions.ExerciseException;
import com.example.fitness_trAIner.common.exception.exceptions.FileStoreException;
import com.example.fitness_trAIner.common.exception.exceptions.NoteException;
import com.example.fitness_trAIner.repository.exercise.Exercise;
import com.example.fitness_trAIner.repository.exercise.ExerciseRepository;
import com.example.fitness_trAIner.repository.exercise.ExerciseVideo;
import com.example.fitness_trAIner.repository.exercise.ExerciseVideoRepository;
import com.example.fitness_trAIner.repository.workout.Note;
import com.example.fitness_trAIner.repository.workout.WorkoutVideo;
import com.example.fitness_trAIner.service.exercise.dto.request.ExerciseServiceSaveRequest;
import com.example.fitness_trAIner.service.exercise.dto.request.ExerciseServiceUpdateRequest;
import com.example.fitness_trAIner.service.exercise.dto.request.ExerciseServiceVideoStreamRequest;
import com.example.fitness_trAIner.service.exercise.dto.response.ExerciseServiceFindListResponse;
import com.example.fitness_trAIner.service.exercise.dto.response.ExerciseServiceSaveResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Service
@Slf4j
public class ExerciseServiceImp implements ExerciseService{
    private final ExerciseRepository exerciseRepository;
    private final ExerciseVideoRepository exerciseVideoRepository;
    @Value("${videopath.admin}")
    private String uploadDir;
    @Override
    public ExerciseServiceSaveResponse saveExercise(ExerciseServiceSaveRequest request) {
        if (exerciseRepository.existsByExerciseName(request.getExerciseName()))
            throw new ExerciseException("이미 존재한는 운동 이름");
        Exercise exercise;
        try {
            exercise = exerciseRepository.save(Exercise.builder()
                    .exerciseName(request.getExerciseName())
                    .exerciseType(request.getExerciseType())
                    .perKcal(request.getPerKcal())
                    .build());

        }catch (Exception e) {
            throw new ExerciseException("예시 운동 정보 저장 오류");
        }
        return ExerciseServiceSaveResponse.builder()
                .exerciseName(exercise.getExerciseName())
                .build();
    }

    @Override
    public ExerciseServiceFindListResponse findAllExercise() {
        List<Exercise> exerciseList = exerciseRepository.findAll();

        return ExerciseServiceFindListResponse.builder()
                .exerciseList(exerciseList)
                .build();
    }

    @Override
    public String updateExercise(ExerciseServiceUpdateRequest request) {
        Exercise exercise = exerciseRepository.findByExerciseName(request.getPreviousName()).orElseThrow(()->new ExerciseException("존재하지 않는 운동"));
//        ExerciseVideo exerciseVideo = new ExerciseVideo();

        try {
//            if (exerciseVideoRepository.existsByExerciseName(request.getPreviousName()))
//                exerciseVideo = exerciseVideoRepository.findByExerciseName(request.getPreviousName()).orElseThrow(()->new ExerciseException("운동 영상 못찾음"));
//                exerciseVideo.setExerciseName(request.getExerciseName());
//                exerciseVideoRepository.save(exerciseVideo);
            exercise.setExerciseName(request.getExerciseName());
            exercise.setPerKcal(request.getPerKcal());
            exercise.setExerciseType(request.getExerciseType());
            exerciseRepository.save(exercise);
        } catch (Exception e) {
            throw new ExerciseException("운동 내용 저장 실패");
        }
        return "수정 성공";
    }

    @Override
    public String deleteExercise(String exerciseName) {

        Exercise exercise = exerciseRepository.findByExerciseName(exerciseName).orElseThrow(()-> new ExerciseException("존재하지 않는 운동"));
        

        try {
            if (exerciseVideoRepository.existsByExerciseName(exerciseName))
                deleteExerciseVideo(exerciseName);
            exerciseRepository.delete(exercise);
        } catch (Exception e) {
            throw new ExerciseException("운동 정보 삭제 실패");
        }
        return "운동 정보 삭제 성공";
    }

    @Override
    public void exerciseVideoStream(ExerciseServiceVideoStreamRequest request) throws IOException {
        ExerciseVideo exerciseVideo = exerciseVideoRepository.findByExerciseName(request.getExerciseName()).orElseThrow(()->new ExerciseException("동영상 없음"));

        File file = new File(exerciseVideo.getFileName());
        if(!file.exists()) throw new FileNotFoundException();

        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");

        long rangeStart = 0;
        long rangeEnd = 0;
        boolean isPart = false;

        try {
            long movieSize = randomAccessFile.length();
            String range = request.getRequest().getHeader("range");

            request.getResponse().reset();
            request.getResponse().setContentType("video/mp4");
            if(range != null) {
                if(range.endsWith("-")){
                    range = range+(movieSize - 1);
                }
                int idxm = range.trim().indexOf("-");
                rangeStart = Long.parseLong(range.substring(6,idxm));
                rangeEnd = Long.parseLong(range.substring(idxm+1));
                if(rangeStart > 0){
                    isPart = true;
                }
            }else{
                rangeStart = 0;
                rangeEnd = movieSize - 1;
            }
            long partSize = rangeEnd - rangeStart + 1;

            request.getResponse().reset();

            request.getResponse().setStatus(isPart ? 206 : 200);

            request.getResponse().setContentType("video/mp4");

            request.getResponse().setHeader("Content-Range", "bytes "+rangeStart+"-"+rangeEnd+"/"+movieSize);
            request.getResponse().setHeader("Accept-Ranges", "bytes");
            request.getResponse().setHeader("Content-Length", ""+partSize);

            OutputStream out = request.getResponse().getOutputStream();
            randomAccessFile.seek(rangeStart);


            int bufferSize = 8*1024;
            byte[] buf = new byte[bufferSize];
            do{
                int block = partSize > bufferSize ? bufferSize : (int)partSize;
                int len = randomAccessFile.read(buf, 0, block);
                out.write(buf, 0, len);
                partSize -= block;
            }while(partSize > 0);

        }catch (IOException e) {

        }finally {
            randomAccessFile.close();
        }

    }

    @Override
    public String exerciseVideoUpload(MultipartFile file, String exerciseName) {

        if (!exerciseRepository.existsByExerciseName(exerciseName)) {
            throw new FileStoreException("존재하지않는 운동 종류");
        }
        if(exerciseVideoRepository.existsByExerciseName(exerciseName)) {
            throw new FileStoreException("해당 운동의 영상이 이미 업로드 되어있음");
        }


        if (!file.getContentType().equals("video/mp4")) {
            throw new FileStoreException("파일 확장자가 알맞지 않습니다.");
        }

        String randomName = UUID.randomUUID().toString();
        Path copyOfLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(randomName+".mp4"));
        File uploadPath = copyOfLocation.toFile();
        uploadPath.mkdirs();

        ExerciseVideo exerciseVideo;

        try {
            System.out.println("file경로 " + copyOfLocation.toString());
            Files.copy(file.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
            exerciseVideo = exerciseVideoRepository.save(ExerciseVideo.builder()
                    .fileName(copyOfLocation.toString())
                    .exerciseName(exerciseName)
                    .build());

            return "파일 저장 성공";
        }catch (IOException e) {
            e.printStackTrace();
            throw new FileStoreException("파일 저장 실패 : " + file.getOriginalFilename());
        }
    }

    @Override
    public String deleteExerciseVideo(String exerciseName) {
        ExerciseVideo exerciseVideo = exerciseVideoRepository.findByExerciseName(exerciseName).orElseThrow(()->new ExerciseException("동영상 없음"));
        exerciseVideoRepository.delete(exerciseVideo);
        File file = new File(exerciseVideo.getFileName());
        try{
            file.delete();
        }catch (Exception e){
            throw new FileStoreException("영상 삭제 실패");
        }


        return "운동 영상 삭제 성공";
    }


}
