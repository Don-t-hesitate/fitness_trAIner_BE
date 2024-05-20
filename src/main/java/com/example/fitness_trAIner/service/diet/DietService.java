package com.example.fitness_trAIner.service.diet;

import com.example.fitness_trAIner.service.diet.dto.request.DietServiceRecommendRequest;
import com.example.fitness_trAIner.service.diet.dto.response.DietServiceRecommendResponse;

import java.io.IOException;

public interface DietService {
    DietServiceRecommendResponse recommendDiet(DietServiceRecommendRequest request) throws IOException;
}
