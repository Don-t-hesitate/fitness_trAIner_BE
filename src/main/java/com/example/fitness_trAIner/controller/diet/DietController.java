package com.example.fitness_trAIner.controller.diet;

import com.example.fitness_trAIner.common.response.GlobalExceptionResponse;
import com.example.fitness_trAIner.common.response.GlobalResponse;
import com.example.fitness_trAIner.service.diet.DietService;
import com.example.fitness_trAIner.service.diet.dto.request.DietServiceSaveDayOfUsersRequest;
import com.example.fitness_trAIner.service.diet.dto.request.DietServiceRecommendRequest;
import com.example.fitness_trAIner.service.diet.dto.response.DietServiceRecommendResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequestMapping("/diets")
@Tag(name = "Diet", description = "식단 관련 API")
@RequiredArgsConstructor
@RestController
public class DietController {

    private final DietService dietService;

    @GetMapping("/{userId}")
    @Operation(summary = "식단 추천 받기", description = "사용자 ID로 DB를 조회, 선호 음식 5가지를 바탕으로 식단 추천")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<DietServiceRecommendResponse> getRecommendedDiet(@PathVariable Long userId) throws IOException {


        return GlobalResponse.<DietServiceRecommendResponse>builder()
                .message("식단 추천 성공")
                .result(dietService.recommendDiet(DietServiceRecommendRequest.builder()
                        .userId(userId)
                        .build()))
                .build();

    }

    @GetMapping("/{userId}/{dietDate}")
    @Operation(summary = "식단 조회", description = "식단 조회 API")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<List<Map>> findDietOfDay(@PathVariable Long userId, @PathVariable String dietDate) throws IOException {

        return GlobalResponse.<List<Map>>builder()
                .message("식단 조회 성공")
                .result(dietService.findDietOfDay(userId, dietDate))
                .build();
    }

    @PostMapping("/{userId}")
    @Operation(summary = "식단 저장", description = "식단 저장 API")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> saveDiet(@RequestBody DietServiceSaveDayOfUsersRequest requestBody) throws IOException {

        return GlobalResponse.<String>builder()
                .message("식단 저장 성공")
                .result(dietService.saveDiet(requestBody))
                .build();
    }

    @GetMapping("/score/{userId}/{date}")
    @Operation(summary = "식단 점수 조회", description = "해당 일자의 운동으로 소모한 총 칼로리를 해당 일자에 섭취한 총 칼로리에서 뺀 값 \n 즉 (섭취 칼로리 - 소모 칼로리)")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<Integer> findDietScore(@PathVariable Long userId, @PathVariable String date) throws IOException {

        return GlobalResponse.<Integer>builder()
                .message("식단 점수 조회 성공")
                .result(dietService.findDietScore(userId, date))
                .build();
    }

}
