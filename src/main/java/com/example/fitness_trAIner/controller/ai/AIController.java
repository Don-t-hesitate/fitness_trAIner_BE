package com.example.fitness_trAIner.controller.ai;

import com.example.fitness_trAIner.common.response.GlobalExceptionResponse;
import com.example.fitness_trAIner.common.response.GlobalResponse;
import com.example.fitness_trAIner.controller.ai.dto.request.AIRequestBody;
import com.example.fitness_trAIner.service.ai.AIService;
import com.example.fitness_trAIner.service.ai.dto.response.AIServiceResponse;
import com.example.fitness_trAIner.service.exercise.dto.request.ExerciseServiceSaveRequest;
import com.example.fitness_trAIner.service.exercise.dto.response.ExerciseServiceSaveResponse;
import com.example.fitness_trAIner.vos.AI.PositionVO;
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
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

@RequestMapping("/ai")
@Tag(name = "AI", description = "ai관련 API")
@RequiredArgsConstructor
@RestController
public class AIController {

    private final AIService aiService;

    @PostMapping("/feedback")
    @Operation(summary = "피드백 인덱스 전송", description = "피드백 보여주기")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = AIServiceResponse.class)))
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<AIServiceResponse> sendAIData(@RequestBody AIRequestBody data) throws IOException {


        ObjectMapper objectMapper = new ObjectMapper();
        String json = new String();
        try {
            json = objectMapper.writeValueAsString(data);


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return GlobalResponse.<AIServiceResponse>builder()
                .message("피드백 제공")
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

    @GetMapping(path = "/pose")
    @Operation(summary = "자세 데이터 운동 명단 조회", description = "학습용 데이터 명단 조회")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<List<String>> getPoseList() {
        return GlobalResponse.<List<String>>builder()
                .message("자세 데이터 운동 명단 조회")
                .result(aiService.getFilesName(""))
                .build();
    }

    @GetMapping(path = "/pose/{exerciseType}")
    @Operation(summary = "자세 데이터 목록 조회", description = "학습용 데이터 목록 조회")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<List<String>> getPoseName(@PathVariable String exerciseType) {
        if (exerciseType.contains("-")) {
            exerciseType = exerciseType.replace("-", "&");
        }
        return GlobalResponse.<List<String>>builder()
                .message("자세 데이터 목록 조회")
                .result(aiService.getFilesName(exerciseType))
                .build();
    }

    @GetMapping(path = "/pose/{exerciseName}/{dataType}")
    @Operation(summary = "자세 데이터 파일 이름 조회", description = "학습용 데이터 파일 이름 조회")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<List<String>> getPoseFileName(@PathVariable String exerciseName, @PathVariable String dataType) {
        return GlobalResponse.<List<String>>builder()
                .message("자세 데이터 파일 이름 조회")
                .result(aiService.getFilesName(exerciseName + File.separator + dataType))
                .build();
    }

    @GetMapping(path = "/pose/{exerciseName}/{dataType}/{fileName}")
    @Operation(summary = "자세 데이터 상세 조회", description = "학습용 데이터 상세 조회")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final ResponseEntity<byte[]> downloadPoseData(@PathVariable String exerciseName, @PathVariable String dataType, @PathVariable String fileName) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            aiService.filesView(exerciseName + File.separator + dataType, fileName, baos);
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

    @DeleteMapping(path = "/pose/{exerciseName}/{dataType}/{fileName}")
    @Operation(summary = "자세 데이터 삭제", description = "학습용 데이터 삭제")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> deletePoseData(@PathVariable String exerciseName, @PathVariable String dataType, @PathVariable String fileName) {
        try {
            return GlobalResponse.<String>builder()
                    .message("자세 데이터 삭제")
                    .result(aiService.deleteFiles(exerciseName + File.separator + dataType, fileName))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @MessageMapping("/start")
    public void start(String requestData) throws Exception { // AI 학습 시작, WebSocket 통신
        ObjectMapper objectMapper = new ObjectMapper();
        String pythonFilePath = objectMapper.readTree(requestData).get("pythonFilePath").asText();
        String params = objectMapper.readTree(requestData).get("params").toString();
        String exerciseName = objectMapper.readTree(requestData).get("exerciseName").asText();

        aiService.startTraining(pythonFilePath, exerciseName, params);
//        aiService.startTraining(pythonFilePath, exerciseName);
    }

    @GetMapping(path = "/exercise/list")
    @Operation(summary = "운동 AI 목록 조회", description = "운동 AI 목록 조회")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<List<String>> viewExerciseList() {
        return GlobalResponse.<List<String>>builder()
                .message("운동 AI 목록 조회")
                .result(aiService.getModelList())
                .build();
    }

    @GetMapping(path = "/exercise/{exerciseName}")
    @Operation(summary = "운동 AI 정보 조회", description = "운동 AI 정보 조회")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<Map<String, List<String>>> viewExerciseInfo(@PathVariable String exerciseName) {
        return GlobalResponse.<Map<String, List<String>>>builder()
                .message("운동 AI 정보 조회")
                .result(aiService.getModelInfo(exerciseName))
                .build();
    }

    @GetMapping(path = "/exercise/{exerciseName}/{modelVersion}")
    @Operation(summary = "운동 AI 상세 정보 조회", description = "운동 AI 상세 정보 조회")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<Map<String, Object>> viewExerciseDetail(@PathVariable String exerciseName, @PathVariable String modelVersion) {
        return GlobalResponse.<Map<String, Object>>builder()
                .message("운동 AI 상세 정보 조회")
                .result(aiService.getModelDetail(exerciseName, modelVersion))
                .build();
    }

    @PostMapping(path = "/exercise/apply/{exerciseName}/{modelVersion}")
    @Operation(summary = "운동 AI 적용", description = "운동 AI 적용")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> applyExercise(@PathVariable String exerciseName, @PathVariable String modelVersion) {
        try {
            return GlobalResponse.<String>builder()
                    .message("운동 AI 적용")
                    .result(aiService.applyModel(exerciseName, modelVersion, ""))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping(path = "/exercise/{exerciseType}/{exerciseName}")
    @Operation(summary = "운동 AI 삭제", description = "운동 AI 삭제")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> deleteExercise(@PathVariable String exerciseType, @PathVariable String exerciseName) {
        try {
            if (exerciseType.contains("-")) {
                exerciseType = exerciseType.replace("-", "&");
            }
            System.out.println("exerciseName: " + exerciseName);
            return GlobalResponse.<String>builder()
                    .message("운동 AI 삭제")
                    .result(aiService.deleteFiles(exerciseType, exerciseName))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "/exercise/download/{exerciseName}/{exerciseVersion}")
    @Operation(summary = "운동 AI 모델 다운로드", description = "운동 AI 모델 다운로드")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final ResponseEntity<byte[]> downloadExerciseModel(@PathVariable String exerciseName, @PathVariable String exerciseVersion) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        aiService.downloadModel(exerciseName, exerciseVersion, baos);
        byte[] zipBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "files.zip");
        headers.setContentLength(zipBytes.length);

        return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);
    }
}
