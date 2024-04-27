package com.example.fitness_trAIner.controller.workout;

import com.example.fitness_trAIner.common.response.GlobalExceptionResponse;
import com.example.fitness_trAIner.common.response.GlobalResponse;
import com.example.fitness_trAIner.controller.workout.dto.request.WorkoutSaveWokroutRequestBody;
import com.example.fitness_trAIner.service.workout.WorkoutService;
import com.example.fitness_trAIner.service.workout.dto.request.WorkoutServiceSaveWorkoutRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/workouts")
@Tag(name = "WorkOut", description = "유저 개인 운동관련 API")
@RequiredArgsConstructor
@RestController
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "동영상 전송", description = "유저 개인운동영상 전송.")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> uploadUserVideo(@RequestPart MultipartFile file) {
        workoutService.fileUpload(file);
        return GlobalResponse.<String>builder()
                .message("유저 운동영상 전송")
                .result("전송 성공")
                .build();
    }
    //TODO 노트 업로드 필요
    @PostMapping("/note/{id}")
    @Operation(summary = "운동일지 업로드", description = "유저 노트 생성(유저 운동 시작시)")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> uploadUserNote(@PathVariable Long id) {
        return GlobalResponse.<String>builder()
                .message("유저 운동일지 생성")
                //FIXME 노트 아이디를 response 해줘야한다.
                .result(workoutService.saveNote(id))
                .build();
    }

    @PostMapping("/note/workout")
    @Operation(summary = "운동 내용 업로드", description = "운동 결과 일지에 업로드(운동이 끝난후)")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> uploadUserNote(@RequestBody WorkoutSaveWokroutRequestBody requestBody) {
        return GlobalResponse.<String>builder()
                .message("유저 운동내용 저장")
                .result(workoutService.saveWorkout(WorkoutServiceSaveWorkoutRequest.builder()
                        .noteId(requestBody.getNoteId())
                        .workoutList(requestBody.getWorkoutList())
                        .build()))
                .build();
    }


}
