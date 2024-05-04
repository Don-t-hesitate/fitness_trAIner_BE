package com.example.fitness_trAIner.service.score;

import com.example.fitness_trAIner.service.score.dto.response.ScoreServiceTop10Response;
import com.example.fitness_trAIner.service.score.dto.response.ScoreServiceUserRankingResponse;

public interface ScoreService {
    public ScoreServiceTop10Response findTop10Ranking(String exerciseName);

    public ScoreServiceUserRankingResponse findUserRanking(String exerciseName, Long userId);

}
