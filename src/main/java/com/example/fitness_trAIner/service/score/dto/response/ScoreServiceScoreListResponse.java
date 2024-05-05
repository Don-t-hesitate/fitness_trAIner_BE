package com.example.fitness_trAIner.service.score.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ScoreServiceScoreListResponse {
    private List<String> scoreCategory;
}
