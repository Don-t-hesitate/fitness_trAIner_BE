package com.example.fitness_trAIner.controller.diet.dto.request;

import com.example.fitness_trAIner.vos.DietVO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class DietSaveDayOfUsersRequestBody {
    List<DietVO> dietList;
}
