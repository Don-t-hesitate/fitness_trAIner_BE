package com.example.fitness_trAIner.controller.ai;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
}
