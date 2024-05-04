package com.example.fitness_trAIner.controller.exercise;


import com.example.fitness_trAIner.common.response.GlobalExceptionResponse;
import com.example.fitness_trAIner.common.response.GlobalResponse;
import com.example.fitness_trAIner.controller.exercise.dto.request.ExerciseSaveRequestBody;
import com.example.fitness_trAIner.controller.exercise.dto.request.ExerciseUpdateRequestBody;
import com.example.fitness_trAIner.service.exercise.ExerciseService;
import com.example.fitness_trAIner.service.exercise.dto.request.ExerciseServiceSaveRequest;
import com.example.fitness_trAIner.service.exercise.dto.request.ExerciseServiceUpdateRequest;
import com.example.fitness_trAIner.service.exercise.dto.request.ExerciseServiceVideoStreamRequest;
import com.example.fitness_trAIner.service.exercise.dto.response.ExerciseServiceFindListResponse;
import com.example.fitness_trAIner.service.exercise.dto.response.ExerciseServiceSaveResponse;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceSaveVideoRequest;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceVideoStreamRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/exercises")
@Tag(name = "Exercise", description = "운동 예시 관련 API")
@RequiredArgsConstructor
@RestController
public class ExerciseController {
    private final ExerciseService exerciseService;

    @PostMapping
    @Operation(summary = "운동 정보 저장", description = "운동 정보 저장 API")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<ExerciseServiceSaveResponse> saveUser(@RequestBody ExerciseSaveRequestBody requestBody) {

        return GlobalResponse.<ExerciseServiceSaveResponse>builder()
                .message("운동 정보 저장")
                .result(exerciseService.saveExercise(ExerciseServiceSaveRequest.builder()
                                .exerciseName(requestBody.getExerciseName())
                                .exerciseType(requestBody.getExerciseType())
                                .perKcal(requestBody.getPerKcal())
                                .build()))
                .build();
    }
    @GetMapping
    @Operation(summary = "운동 정보 조회", description = "운동 정보 조회 API")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ExerciseServiceFindListResponse.class)))
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<ExerciseServiceFindListResponse> findAllExercise() {

        return GlobalResponse.<ExerciseServiceFindListResponse>builder()
                .message("운동 정보 입력")
                .result(exerciseService.findAllExercise())
                .build();
    }
    @PutMapping
    @Operation(summary = "운동 정보 수정", description = "운동 정보 수정 API")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> updateExercise(@RequestBody ExerciseUpdateRequestBody requestBody) {

        return GlobalResponse.<String>builder()
                .message("운동 정보 수정")
                .result(exerciseService.updateExercise(ExerciseServiceUpdateRequest.builder()
                        .previousName(requestBody.getExerciseName())
                        .exerciseName(requestBody.getNewExerciseName())
                        .exerciseType(requestBody.getNewExerciseType())
                        .perKcal(requestBody.getNewPerKcal())
                        .build()))
                .build();
    }

    @DeleteMapping("/{exerciseName}")
    @Operation(summary = "운동 정보 삭제", description = "운동 정보 삭제 API")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> deleteExercise(@PathVariable String exerciseName) {

        return GlobalResponse.<String>builder()
                .message("운동 정보 입력")
                .result(exerciseService.deleteExercise(exerciseName))
                .build();
    }
    @RequestMapping(value = "/video/stream/{exerciseName}", method = RequestMethod.GET)
    @Operation(summary = "운동 예시 영상 스트리밍@@스웨거에서 사용 금지", description = "운동 예시 영상 스트리밍 API 스웨거에서 사용하면 멈춤, 운동 이름으로 조회")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final void videoStream(@PathVariable String exerciseName,
                                  HttpServletResponse response, HttpServletRequest request) throws IOException {
        exerciseService.exerciseVideoStream(ExerciseServiceVideoStreamRequest.builder()
                .exerciseName(exerciseName)
                .request(request)
                .response(response)
                .build());
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "video/{exerciseName}")
    @Operation(summary = "동영상 업로드", description = "운동 예시 영상 업로드 C:/video/exercise 이경로에 저장")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> uploadExerciseVideo(@RequestPart MultipartFile file, @PathVariable String exerciseName) {

        return GlobalResponse.<String>builder()
                .message("유저 운동영상 전송")
                .result(exerciseService.exerciseVideoUpload(file, exerciseName))
                .build();
    }
    @DeleteMapping("/video/{exerciseName}")
    @Operation(summary = "운동 영상 삭제", description = "운동 영상 삭제 API")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> deleteExerciseVideo(@PathVariable String exerciseName) {

        return GlobalResponse.<String>builder()
                .message("운동 영상 삭제")
                .result(exerciseService.deleteExerciseVideo(exerciseName))
                .build();
    }

}
