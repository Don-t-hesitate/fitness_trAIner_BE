package com.example.fitness_trAIner.service.score;

import com.example.fitness_trAIner.service.score.dto.response.ScoreServiceTop10Response;

public interface ScoreService {
    public ScoreServiceTop10Response findTop10Ranking(String exerciseName);
}
