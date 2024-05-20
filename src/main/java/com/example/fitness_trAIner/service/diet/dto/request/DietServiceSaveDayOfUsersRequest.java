package com.example.fitness_trAIner.service.diet.dto.request;

import com.example.fitness_trAIner.vos.DietVO;
import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class DietServiceSaveDayOfUsersRequest {
    // 사용자 ID, 식단 날짜, 음식 ID, 총 칼로리를 리스트로 받기
    List<DietVO> dietList;
}
