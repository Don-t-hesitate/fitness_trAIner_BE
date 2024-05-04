package com.example.fitness_trAIner.service.score.dto.response;

import com.example.fitness_trAIner.repository.user.UserScore;
import com.example.fitness_trAIner.vos.UserScoreVO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ScoreServiceTop10Response {
    private List<UserScoreVO> userScoreVOList;
}
