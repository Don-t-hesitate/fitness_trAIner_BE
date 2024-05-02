package com.example.fitness_trAIner.controller.workout;

import com.example.fitness_trAIner.common.response.GlobalExceptionResponse;
import com.example.fitness_trAIner.common.response.GlobalResponse;
import com.example.fitness_trAIner.controller.workout.dto.request.WorkoutSaveWokroutRequestBody;
import com.example.fitness_trAIner.repository.workout.WorkoutVideo;
import com.example.fitness_trAIner.repository.workout.WorkoutVideoRepository;
import com.example.fitness_trAIner.service.workout.WorkoutService;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceFindNoteListRequest;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceSaveVideoRequest;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceSaveWorkoutRequest;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceVideoStreamRequest;
import com.example.fitness_trAIner.service.workout.dto.response.WorkoutServiceFindNoteDetailResponse;
import com.example.fitness_trAIner.service.workout.dto.response.WorkoutServiceFindNoteListResponse;
import com.example.fitness_trAIner.service.workout.dto.response.WorkoutServiceFindVideoListResponse;
import com.example.fitness_trAIner.service.workout.dto.response.WorkoutServiceSaveNoteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.util.Date;

@RequestMapping("/workouts")
@Tag(name = "WorkOut", description = "유저 개인 운동관련 API")
@RequiredArgsConstructor
@RestController
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "video/{noteId}/{exerciseName}")
    @Operation(summary = "동영상 업로드", description = "유저 개인운동영상 업로드(운동이 끝난후, 운동일지 업로드가 된상태면 업로드 가능) 테스트단계에서는 C:/video/workout 이경로에 저장")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> uploadUserVideo(@RequestPart MultipartFile file, @PathVariable Long noteId, @PathVariable String exerciseName) {

        return GlobalResponse.<String>builder()
                .message("유저 운동영상 전송")
                .result(workoutService.fileUpload(file, WorkoutServiceSaveVideoRequest.builder()
                        .noteId(noteId)
                        .exerciseName(exerciseName)
                        .build()))
                .build();
    }

    @PostMapping("/note/{userId}")
    @Operation(summary = "운동일지 업로드", description = "유저 노트 생성(유저 운동 시작시)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = WorkoutServiceSaveNoteResponse.class)))
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<WorkoutServiceSaveNoteResponse> uploadUserNote(@PathVariable Long userId) {
        return GlobalResponse.<WorkoutServiceSaveNoteResponse>builder()
                .message("유저 운동일지 생성")
                .result(workoutService.saveNote(userId))
                .build();
    }

    @PostMapping("/note/workout")
    @Operation(summary = "운동 내용 업로드", description = "운동 결과 일지에 업로드(해당운동의 세트가 모두 끝난후)")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> uploadUserNote(@RequestBody WorkoutSaveWokroutRequestBody requestBody) {
        return GlobalResponse.<String>builder()
                .message("유저 운동내용 저장")
                .result(workoutService.saveWorkout(WorkoutServiceSaveWorkoutRequest.builder()
                        .noteId(requestBody.getNoteId())
                        .exerciseName(requestBody.getExerciseName())
                        .workoutList(requestBody.getWorkoutList())
                        .build()))
                .build();
    }

    @GetMapping("/note/workout/{userId}/{date}")
    @Operation(summary = "운동 일지 목록 조회", description = "날짜를 이용한 일지 데이터 전체(해당날짜) 조회(Date폼은 '2024-05-02')")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<WorkoutServiceFindNoteListResponse> findNoteListByDate(@PathVariable Long userId, @PathVariable LocalDate date) {
        return GlobalResponse.<WorkoutServiceFindNoteListResponse>builder()
                .message("운동 일지 리스트 조회")
                .result(workoutService.findNoteList(WorkoutServiceFindNoteListRequest.builder()
                        .userId(userId)
                        .date(date)
                        .build()))
                .build();
    }


    @RequestMapping(value = "/video/stream/{workoutVideoId}", method = RequestMethod.GET)
    @Operation(summary = "사용자 영상 스트리밍@@스웨거에서 사용 금지", description = "사용자 영상 스트리밍 API 스웨거에서 사용하면 멈춤")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final void videoStream(@PathVariable Long workoutVideoId,
                                  HttpServletResponse response, HttpServletRequest request) throws IOException {
//        WorkoutVideo workoutVideo = workoutVideoRepository.findByWorkoutVideoId(workoutVideoId);
//
//        File file = new File(workoutVideo.getFileName());
//        if(!file.exists()) throw new FileNotFoundException();
//
//        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
//
//        long rangeStart = 0;
//        long rangeEnd = 0;
//        boolean isPart = false;
//
//        try {
//            long movieSize = randomAccessFile.length();
//            String range = request.getHeader("range");
//
//            response.reset();
//            response.setContentType("video/mp4");
//            if(range != null) {
//                if(range.endsWith("-")){
//                    range = range+(movieSize - 1);
//                }
//                int idxm = range.trim().indexOf("-");
//                rangeStart = Long.parseLong(range.substring(6,idxm));
//                rangeEnd = Long.parseLong(range.substring(idxm+1));
//                if(rangeStart > 0){
//                    isPart = true;
//                }
//            }else{
//                rangeStart = 0;
//                rangeEnd = movieSize - 1;
//            }
//            long partSize = rangeEnd - rangeStart + 1;
//
//            response.reset();
//
//            response.setStatus(isPart ? 206 : 200);
//
//            response.setContentType("video/mp4");
//
//            response.setHeader("Content-Range", "bytes "+rangeStart+"-"+rangeEnd+"/"+movieSize);
//            response.setHeader("Accept-Ranges", "bytes");
//            response.setHeader("Content-Length", ""+partSize);
//
//            OutputStream out = response.getOutputStream();
//            randomAccessFile.seek(rangeStart);
//
//
//            int bufferSize = 8*1024;
//            byte[] buf = new byte[bufferSize];
//            do{
//                int block = partSize > bufferSize ? bufferSize : (int)partSize;
//                int len = randomAccessFile.read(buf, 0, block);
//                out.write(buf, 0, len);
//                partSize -= block;
//            }while(partSize > 0);
//
//        }catch (IOException e) {
//
//        }finally {
//            randomAccessFile.close();
//        }
//
//
//    }
        workoutService.workoutVideoStream(WorkoutServiceVideoStreamRequest.builder()
                .workoutVideoId(workoutVideoId)
                .request(request)
                .response(response)
                .build());
    }

    @GetMapping("/video/{noteId}")
    @Operation(summary = "운동 영상 리스트 조회", description = "노트 아이디를 사용한 영상 리스트 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = WorkoutServiceFindVideoListResponse.class)))
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<WorkoutServiceFindVideoListResponse> findNoteListByDate(@PathVariable Long noteId) {
        return GlobalResponse.<WorkoutServiceFindVideoListResponse>builder()
                .message("운동 영상 리스트 조회")
                .result(workoutService.findWorkoutVideoListByNoteId(noteId))
                .build();
    }
    @GetMapping("/note/{noteId}")
    @Operation(summary = "운동 일지 상세조회", description = "노트 아이디를 사용한 노트 상세조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = WorkoutServiceFindNoteDetailResponse.class)))
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<WorkoutServiceFindNoteDetailResponse> findNoteDetail(@PathVariable Long noteId) {
        return GlobalResponse.<WorkoutServiceFindNoteDetailResponse>builder()
                .message("운동일지 상세조회")
                .result(workoutService.findNoteDetail(noteId))
                .build();
    }
}
