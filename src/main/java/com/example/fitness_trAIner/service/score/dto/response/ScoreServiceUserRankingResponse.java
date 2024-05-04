package com.example.fitness_trAIner.service.score.dto.response;

import com.example.fitness_trAIner.vos.UserScoreVO;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ScoreServiceUserRankingResponse {
    private String exerciseName;
    private Long ranking;
    private UserScoreVO userScoreVO;

}
