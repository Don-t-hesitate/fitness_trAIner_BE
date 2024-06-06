package com.example.fitness_trAIner.service.diet;

import com.example.fitness_trAIner.service.diet.dto.request.DietServiceRecommendRequest;
import com.example.fitness_trAIner.service.diet.dto.request.DietServiceSaveDayOfUsersRequest;
import com.example.fitness_trAIner.service.diet.dto.response.DietServiceRecommendResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DietService {
    DietServiceRecommendResponse recommendDiet(DietServiceRecommendRequest request) throws IOException;
    Map<String, List> findDietOfDay(Long userId, String dietDate);
    String saveDiet(DietServiceSaveDayOfUsersRequest requestBody);
    String saveRecommendDiet(DietServiceRecommendRequest requestBody);
}
