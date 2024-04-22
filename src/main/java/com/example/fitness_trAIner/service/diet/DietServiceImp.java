package com.example.fitness_trAIner.service.diet;

import com.example.fitness_trAIner.common.exception.exceptions.InvalidCategoryException;
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

        foodList = switch (category) {
            case "korean" -> Arrays.asList("흑미밥", "된장찌개", "제육볶음", "순살 후라이드 치킨", "김치");
            case "chinese" -> Arrays.asList("짜장면", "탕수육", "마라탕", "유산슬", "제로 탕후루");
            case "japanese" -> Arrays.asList("스시", "우메보시", "타코야끼", "단무지", "사케");
            case "western" -> Arrays.asList("라따뚜이", "블랙 푸딩", "알리오 올리오", "포케", "피시 앤 칩스");
            case "fastfood" -> Arrays.asList("샌드위치", "햄버거", "라면", "떡볶이", "피자");
            case "dessert" -> Arrays.asList("버블티", "치즈케이크", "마카롱", "티라미수", "와플");
            default -> throw new InvalidCategoryException("Unexpected value: " + category);
        };

        return DietServiceRecommendResponse.builder()
                .foodRecommend(foodList)
                .totalCalorie(totalCalorie)
                .build();

    }
}
