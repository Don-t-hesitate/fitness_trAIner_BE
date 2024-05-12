package com.example.fitness_trAIner.controller.ai;

import com.example.fitness_trAIner.common.exception.exceptions.EmptyDirectoryException;
import com.example.fitness_trAIner.common.response.GlobalExceptionResponse;
import com.example.fitness_trAIner.common.response.GlobalResponse;
import com.example.fitness_trAIner.controller.ai.dto.request.AIRequestBody;
import com.example.fitness_trAIner.controller.exercise.dto.request.ExerciseSaveRequestBody;
import com.example.fitness_trAIner.service.ai.AIService;
import com.example.fitness_trAIner.service.exercise.dto.request.ExerciseServiceSaveRequest;
import com.example.fitness_trAIner.service.exercise.dto.response.ExerciseServiceSaveResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

@RequestMapping("/ai")
@Tag(name = "AI", description = "ai관련 API")
@RequiredArgsConstructor
@RestController
public class AIController {

    private final AIService aiService;

    @PostMapping
    @Operation(summary = "AI데이터 전송", description = "사람의 각 부위의 포지션 전달")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> sendAIData(@RequestBody AIRequestBody data) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String json = new String();
        try {
            json = objectMapper.writeValueAsString(data);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return GlobalResponse.<String>builder()
                .message("AI데이터 전송")
                .result(aiService.pythonProcess(json))
                .build();
    }

//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE + "; charset=UTF-8", path = "/pose")
    @PostMapping(path = "/pose", headers = ("content-type=multipart/form-data;charset=UTF-8"))
    @Operation(summary = "자세 데이터 전송", description = "학습용 데이터를 서버에 저장<br></br><strong>files</strong>는 여러가지 파일을 한꺼번에 전송 가능<br></br><strong>parentPath</strong>는 상위 경로 지정(Bodyweight, Dumbbell&barbell, Machine 등)<br></br><strong>uploadPath</strong>는 저장될 하위 경로 지정(운동이름)")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> savePoseData(List<MultipartFile> files, @RequestPart("parentPath") String parentPath, @RequestPart("uploadPath") String uploadPath) {

        try {
            String decodedUploadPath = URLDecoder.decode(uploadPath, "UTF-8");
            return GlobalResponse.<String>builder()
                    .message("자세 데이터 저장: " + decodedUploadPath)
                    .result(aiService.uploadFiles(files, parentPath, decodedUploadPath))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "/pose/{exerciseType}")
    @Operation(summary = "자세 데이터 명단 조회", description = "학습용 데이터 명단 조회")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<List<String>> viewPoseName(@PathVariable String exerciseType) {
        if (exerciseType.contains("-")) {
            exerciseType = exerciseType.replace("-", "&");
        }
        return GlobalResponse.<List<String>>builder()
                .message("자세 데이터 명단 조회")
                .result(aiService.filesNameView(exerciseType))
                .build();
    }


    @GetMapping(path = "/pose/{exerciseType}/{exerciseName}")
    @Operation(summary = "자세 데이터 상세 조회", description = "학습용 데이터 상세 조회")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final ResponseEntity<byte[]> downloadPoseData(@PathVariable String exerciseType, @PathVariable String exerciseName) {
        try {
            if (exerciseType.contains("-")) {
                exerciseType = exerciseType.replace("-", "&");
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            aiService.filesView(exerciseType, exerciseName, baos);
            byte[] zipBytes = baos.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "files.zip");
            headers.setContentLength(zipBytes.length);

            return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping(path = "/pose/{exerciseType}/{exerciseName}")
    @Operation(summary = "자세 데이터 삭제", description = "학습용 데이터 삭제")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> deletePoseData(@PathVariable String exerciseType, @PathVariable String exerciseName) {
        try {
            if (exerciseType.contains("-")) {
                exerciseType = exerciseType.replace("-", "&");
            }
            return GlobalResponse.<String>builder()
                    .message("자세 데이터 삭제")
                    .result(aiService.deleteFiles(exerciseType, exerciseName))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
