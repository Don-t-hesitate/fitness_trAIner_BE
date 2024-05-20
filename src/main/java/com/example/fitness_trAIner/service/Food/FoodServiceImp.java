package com.example.fitness_trAIner.service.Food;

import com.example.fitness_trAIner.service.Food.dto.response.FoodServiceListForSignUpResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class FoodServiceImp implements FoodService {

    @Value("${foodpath}")
    private String foodPath;

    @Override
    public List<FoodServiceListForSignUpResponse> initFood() {
        // select_food_list.csv 파일에서 음식 리스트 가져오기
        List<FoodServiceListForSignUpResponse> foodList;
        String filePath = foodPath + File.separator + "select_food_list.csv";
        foodList = getFoodList(filePath);

        return foodList;
    }

    private List<FoodServiceListForSignUpResponse> getFoodList(String filePath) {
        List<FoodServiceListForSignUpResponse> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // 첫 줄은 헤더이므로 읽지 않음

            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                FoodServiceListForSignUpResponse food = new FoodServiceListForSignUpResponse();
                food.setFoodName(fields[0]);
                food.setTaste(fields[1]);
                food.setMainIngredient(fields[2]);
                food.setSecondaryIngredient(fields[3]);
                food.setCookMethod(fields[4]);
                rows.add(food);
            }
        } catch (IOException e) {
            log.error("파일 읽는 중 오류 발생 {}", filePath ,e);
        }

        return rows;
    }
}
