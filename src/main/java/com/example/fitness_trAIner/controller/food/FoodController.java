package com.example.fitness_trAIner.controller.food;

import com.example.fitness_trAIner.common.response.GlobalExceptionResponse;
import com.example.fitness_trAIner.common.response.GlobalResponse;
import com.example.fitness_trAIner.service.food.FoodService;
import com.example.fitness_trAIner.service.food.dto.response.FoodServiceListForSignUpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/foods")
@Tag(name = "Food", description = "음식 관련 API")
@RequiredArgsConstructor
@RestController
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/init")
    @Operation(summary = "최초 음식 5개 기입용 음식", description = "회원가입 시 선택할 음식 리스트")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public GlobalResponse<List<FoodServiceListForSignUpResponse>> initFood() {

        return GlobalResponse.<List<FoodServiceListForSignUpResponse>>builder()
                .message("최초 음식 5개 기입 성공")
                .result(foodService.initFood())
                .build();
    }

}
