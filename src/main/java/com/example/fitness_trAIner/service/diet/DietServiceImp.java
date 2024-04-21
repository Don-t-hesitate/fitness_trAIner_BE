package com.example.fitness_trAIner.service.diet;

import com.example.fitness_trAIner.service.diet.dto.request.DietServiceRecommendRequest;
import com.example.fitness_trAIner.service.diet.dto.response.DietServiceRecommendResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DietServiceImp implements DietService{
    @Override
    public DietServiceRecommendResponse recommendDiet(DietServiceRecommendRequest request) {

        String category = request.getCategory();
        List<String> foodList = new ArrayList<>();
        int totalCalorie = 1480; // 예시 값

        if(category.equals("korean")) {
            foodList = Arrays.asList("흑미밥", "된장찌개", "제육볶음", "순살 후라이드 치킨", "김치");
        } else if(category.equals("chinese")) {
            foodList = Arrays.asList("흑미밥", "된장찌개", "제육볶음", "순살 후라이드 치킨", "김치");
        } else if(category.equals("japanese")) {
            foodList = Arrays.asList("흑미밥", "된장찌개", "제육볶음", "순살 후라이드 치킨", "김치");
        } else if(category.equals("western")) {
            foodList = Arrays.asList("흑미밥", "된장찌개", "제육볶음", "순살 후라이드 치킨", "김치");
        } else if (category.equals("dessert")) {
            foodList = Arrays.asList("흑미밥", "된장찌개", "제육볶음", "순살 후라이드 치킨", "김치");
        } else if (category.equals("fastfood")) {
            foodList = Arrays.asList("흑미밥", "된장찌개", "제육볶음", "순살 후라이드 치킨", "김치");
        }

        return DietServiceRecommendResponse.builder()
                .foodRecommend(foodList)
                .totalCalorie(totalCalorie)
                .build();

    }
}
