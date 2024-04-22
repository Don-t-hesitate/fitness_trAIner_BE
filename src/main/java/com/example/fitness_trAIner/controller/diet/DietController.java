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

import java.util.Optional;

@RequestMapping("/diet")
@Tag(name = "Diet", description = "식단 관련 API")
@RequiredArgsConstructor
@RestController
public class DietController {

    private final DietService dietService;

    @GetMapping
    @Operation(summary = "식단 조회", description = "식단 조회 API")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<DietServiceRecommendResponse> findAllDietList(
            @RequestParam(required = false) String category) {

        String validatedCategory = Optional.ofNullable(category)
                .orElseThrow(() -> new InvalidCategoryException("카테고리가 필요합니다."));

        DietServiceRecommendRequest request = DietServiceRecommendRequest.builder()
                .category(validatedCategory)
                .build();

        DietServiceRecommendResponse response = dietService.recommendDiet(request);

        return GlobalResponse.<DietServiceRecommendResponse>builder()
                .message("식단조회")
                .result(response)
                .build();

    }

}
