package com.example.fitness_trAIner.controller.ai.dto.request;

import com.example.fitness_trAIner.vos.AI.PositionVO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AIRequestBody {
    List<PositionVO> positionList;
}
