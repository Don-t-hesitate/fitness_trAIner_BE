package com.example.fitness_trAIner.service.ai.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AIServiceResponse {
    private int perfect;
    private int good;
    private int bad;
    private int score;

}
