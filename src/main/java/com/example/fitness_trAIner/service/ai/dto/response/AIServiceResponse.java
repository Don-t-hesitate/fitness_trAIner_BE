package com.example.fitness_trAIner.service.ai.dto.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AIServiceResponse {
    private int perfect;
    private int good;
    private int bad;
    private List<String> feedback;

}
