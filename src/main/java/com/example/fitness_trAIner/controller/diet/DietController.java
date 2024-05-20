package com.example.fitness_trAIner.controller.diet;

import com.example.fitness_trAIner.common.exception.exceptions.InvalidCategoryException;
import com.example.fitness_trAIner.common.response.GlobalExceptionResponse;
import com.example.fitness_trAIner.common.response.GlobalResponse;
import com.example.fitness_trAIner.controller.diet.dto.request.DietRecommendRequestBody;
import com.example.fitness_trAIner.service.diet.DietService;
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
import java.util.Optional;

@RequestMapping("/diets")
@Tag(name = "Diet", description = "식단 관련 API")
@RequiredArgsConstructor
@RestController
public class DietController {

    private final DietService dietService;

    @PostMapping("/recommend")
    @Operation(summary = "식단 조회", description = "식단 조회 API")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<DietServiceRecommendResponse> findAllDietList(@RequestBody DietRecommendRequestBody requestBody) throws IOException {


        return GlobalResponse.<DietServiceRecommendResponse>builder()
                .message("식단조회")
                .result(dietService.recommendDiet(DietServiceRecommendRequest.builder()
                        .category(requestBody.getCategory())
                        .userId(requestBody.getUserId())
                        .build()))
                .build();

    }

}
