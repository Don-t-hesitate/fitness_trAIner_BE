package com.example.fitness_trAIner.controller.diet;

import com.example.fitness_trAIner.common.response.GlobalExceptionResponse;
import com.example.fitness_trAIner.common.response.GlobalResponse;
import com.example.fitness_trAIner.controller.diet.dto.request.DietRecommendRequestBody;
import com.example.fitness_trAIner.controller.diet.dto.request.DietSaveDayOfUsersRequestBody;
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

    @PostMapping
    @Operation(summary = "식단 추천 받기", description = "최초 선호 음식과 사용자가 기존에 받은 추천 음식을 기반한 식단 추천<br>음식 이름 리스트를 서버에 제공하면 해당 음식을 찾아서 식단 테이블에 저장")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<DietServiceRecommendResponse> getRecommendedDiet(@RequestBody DietRecommendRequestBody requestBody) throws IOException {


        return GlobalResponse.<DietServiceRecommendResponse>builder()
                .message("식단 추천 성공")
                .result(dietService.recommendDiet(DietServiceRecommendRequest.builder()
                        .userId(requestBody.getUserId())
                        .consumedFoodNames(requestBody.getConsumedFoodNames())
                        .build()))
                .build();

    }

    @GetMapping("/{userId}/{dietDate}")
    @Operation(summary = "식단 조회", description = "식단 조회 API")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<Map<String, List>> findDietOfDay(@PathVariable Long userId, @PathVariable String dietDate) {

        return GlobalResponse.<Map<String, List>>builder()
                .message("식단 조회 성공")
                .result(dietService.findDietOfDay(userId, dietDate))
                .build();
    }

    @PostMapping("/{userId}")
    @Operation(summary = "식단 저장(식단 추천 미실행 시)", description = "식단 추천 이외의 식단 저장 API")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> saveDiet(@RequestBody DietSaveDayOfUsersRequestBody requestBody) {

        return GlobalResponse.<String>builder()
                .message("식단 저장 성공")
                .result(dietService.saveDiet(DietServiceSaveDayOfUsersRequest.builder()
                        .dietList(requestBody.getDietList())
                        .build()))
                .build();
    }

    @PostMapping("/select")
    @Operation(summary = "선택된 식단 추천 결과 저장", description = "선택된 식단 추천 결과를 user 테이블의 preference_foods에 저장")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> saveRecommendedDiet(@RequestBody DietRecommendRequestBody requestBody) {

        return GlobalResponse.<String>builder()
                .message("식단 저장 성공")
                .result(dietService.saveRecommendDiet(DietServiceRecommendRequest.builder()
                        .userId(requestBody.getUserId())
                        .consumedFoodNames(requestBody.getConsumedFoodNames())
                        .build()))
                .build();
    }

}
