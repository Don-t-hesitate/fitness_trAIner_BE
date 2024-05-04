package com.example.fitness_trAIner.controller.score;

import com.example.fitness_trAIner.common.response.GlobalExceptionResponse;
import com.example.fitness_trAIner.common.response.GlobalResponse;
import com.example.fitness_trAIner.service.score.ScoreService;
import com.example.fitness_trAIner.service.score.dto.response.ScoreServiceTop10Response;
import com.example.fitness_trAIner.service.score.dto.response.ScoreServiceUserRankingResponse;
import com.example.fitness_trAIner.service.user.dto.response.UserServiceDetailInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/scores")
@Tag(name = "Score", description = "점수 관련 API")
@RequiredArgsConstructor
@RestController
public class ScoreController {
    private final ScoreService scoreService;
    @GetMapping("/{exerciseName}")
    @Operation(summary = "운동이름으로 랭킹 조회", description = "운동 이름으로 상위 10개 랭킹 조회")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true, content = @Content(schema = @Schema(implementation =  ScoreServiceTop10Response.class)))
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<ScoreServiceTop10Response> findTop10ByExerciseName(@PathVariable String exerciseName) {
        return GlobalResponse.<ScoreServiceTop10Response>builder()
                .message("유저 상세 조회")
                .result(scoreService.findTop10Ranking(exerciseName))
                .build();
    }
    @GetMapping("/user/{userId}/{exerciseName}")
    @Operation(summary = "운동이름으로 랭킹 조회", description = "운동 이름으로 상위 10개 랭킹 조회")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true, content = @Content(schema = @Schema(implementation =  ScoreServiceUserRankingResponse.class)))
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<ScoreServiceUserRankingResponse> findUserRanking(@PathVariable String exerciseName, @PathVariable Long userId) {
        return GlobalResponse.<ScoreServiceUserRankingResponse>builder()
                .message("유저 상세 조회")
                .result(scoreService.findUserRanking(exerciseName, userId))
                .build();
    }

}
