package com.example.fitness_trAIner.service.workout;

import com.example.fitness_trAIner.common.exception.exceptions.FileStoreException;
import com.example.fitness_trAIner.common.exception.exceptions.NoUserException;
import com.example.fitness_trAIner.common.exception.exceptions.NoteException;
import com.example.fitness_trAIner.common.exception.exceptions.ScoreException;
import com.example.fitness_trAIner.repository.user.User;
import com.example.fitness_trAIner.repository.user.UserRepository;
import com.example.fitness_trAIner.repository.user.UserScore;
import com.example.fitness_trAIner.repository.user.UserScoreRepository;
import com.example.fitness_trAIner.repository.workout.*;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceFindNoteListRequest;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceSaveVideoRequest;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceSaveWorkoutRequest;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceVideoStreamRequest;
import com.example.fitness_trAIner.service.workout.dto.response.WorkoutServiceFindNoteListResponse;
import com.example.fitness_trAIner.service.workout.dto.response.WorkoutServiceFindVideoListResponse;
import com.example.fitness_trAIner.service.workout.dto.response.WorkoutServiceSaveNoteResponse;
import com.example.fitness_trAIner.vos.WorkoutVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class WorkoutServiceImp implements WorkoutService {
    private final NoteRepository noteRepository;
    private final WorkoutRepository workoutRepository;
    private final WorkoutVideoRepository workoutVideoRepository;
    private final UserScoreRepository userScoreRepository;
    private final UserRepository userRepository;
    @Value("${videopath.user}")
    private String uploadDir;

    @Override
    public String fileUpload(MultipartFile file, WorkoutServiceSaveVideoRequest request) {

        if (!noteRepository.existsByNoteId(request.getNoteId())) {
            throw new FileStoreException("존재하지않는 노트");
        }

        Note note = noteRepository.findByNoteId(request.getNoteId());

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
                    .userId(note.getUserId())
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
    public WorkoutServiceSaveNoteResponse saveNote(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoteException("존재하지 않는 유저 아이디");
        }

        Note note = noteRepository.save(Note.builder().userId(userId).build());


        return WorkoutServiceSaveNoteResponse.builder()
                .noteId(note.getNoteId())
                .build();
    }

    @Override
    public WorkoutServiceFindNoteListResponse findNoteList(WorkoutServiceFindNoteListRequest request) {
        try {
            List<Note> noteList = noteRepository.findAllByUserIdAndWorkoutDate(request.getUserId(), request.getDate());
            return WorkoutServiceFindNoteListResponse.builder()
                    .noteList(noteList)
                    .build();
        }
        catch (Exception e) {
            throw new NoteException("노트 조회 실패");
        }

    }

    @Override
    public String saveWorkout(WorkoutServiceSaveWorkoutRequest request) {
        Note note = noteRepository.findById(request.getNoteId()).orElseThrow(() -> new NoteException("노트 찾기 오류"));
        User user = userRepository.findById(note.getUserId()).orElseThrow(() -> new NoUserException("유저 찾기 오류"));
        int totalScore = 0;
        int totalPerfect = note.getTotalPerfect();
        int totalGood = note.getTotalGood();
        int totalBad = note.getTotalBad();
        try {
            for (WorkoutVO workoutVO : request.getWorkoutList()) {
                Workout workout = new Workout();
                workout.setNoteId(request.getNoteId());
                workout.setExerciseName(request.getExerciseName());
                workout.setSetNum(workoutVO.getSetNum());
                workout.setRepeats(workoutVO.getRepeats());
                
                //FIXME 몸무게 nullable 확인후 수정
                if (workoutVO.getWeight() == 0) {
                    if (user.getWeight() == null)
                        workout.setWeight(70);
                    else {
                        workout.setWeight(user.getWeight().intValue());
                    }

                } else {
                    workout.setWeight(workoutVO.getWeight());
                }
                totalPerfect += workoutVO.getScorePerfect();
                totalGood += workoutVO.getScoreGood();
                totalBad += workoutVO.getScoreBad();
                if (workoutVO.getRepeats() > 0) {
                    totalScore += (workoutVO.getRepeats() * (workoutVO.getScorePerfect() * 3 + workoutVO.getScoreGood() * 2 + workoutVO.getScoreBad()) *
                            (1 + (workoutVO.getWeight() / 100)));
                }
                workoutRepository.save(workout);
            }
        }catch (Exception e) {
            throw new NoteException("노트 저장 오류 saveWorkout");
        }


        try {
            userScoreRepository.save(UserScore.builder()
                    .userId(note.getUserId())
                    .exerciseName(request.getExerciseName())
                    .score(totalScore)
                    .build());

            note.setTotalScore(note.getTotalScore() + totalScore);
            note.setTotalPerfect(totalPerfect);
            note.setTotalGood(totalGood);
            note.setTotalBad(totalBad);
            noteRepository.save(note);
        } catch (Exception e) {
            throw new ScoreException("점수 저장 오류 saveWorkout");
        }
        return "운동 내용 업로드 성공";
    }

    @Override
    public void workoutVideoStream(WorkoutServiceVideoStreamRequest request) throws IOException {

        WorkoutVideo workoutVideo = workoutVideoRepository.findByWorkoutVideoId(request.getWorkoutVideoId());

        File file = new File(workoutVideo.getFileName());
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
    public WorkoutServiceFindVideoListResponse findWorkoutVideoListByNoteId(Long noteId) {
        List<WorkoutVideo> workoutVideoList = workoutVideoRepository.findAllByNoteId(noteId);
        return WorkoutServiceFindVideoListResponse.builder()
                .workoutVideoList(workoutVideoList)
                .build();
    }


}
