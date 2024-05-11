package com.example.fitness_trAIner.service.ai;

import com.example.fitness_trAIner.service.ai.dto.response.AIServiceResponse;

import java.io.IOException;

public interface AIService {
    public AIServiceResponse pythonProcess(String data) throws IOException;
}
