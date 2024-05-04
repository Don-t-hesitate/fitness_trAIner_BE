package com.example.fitness_trAIner.service.score;

import com.example.fitness_trAIner.common.exception.exceptions.NoUserException;
import com.example.fitness_trAIner.repository.user.User;
import com.example.fitness_trAIner.repository.user.UserRepository;
import com.example.fitness_trAIner.repository.user.UserScore;
import com.example.fitness_trAIner.repository.user.UserScoreRepository;
import com.example.fitness_trAIner.service.score.dto.response.ScoreServiceTop10Response;
import com.example.fitness_trAIner.vos.UserScoreVO;
import com.example.fitness_trAIner.vos.UserVO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Service
@Slf4j
public class ScoreServiceImp implements ScoreService {
    private final UserScoreRepository userScoreRepository;
    private final UserRepository userRepository;
    @Override
    public ScoreServiceTop10Response findTop10Ranking(String exerciseName) {
        List<UserScore> userScoreList = userScoreRepository.findTop10ByExerciseNameOrderByScoreDesc(exerciseName);
        List<UserScoreVO> userScoreVOList = new ArrayList<>();

        for (UserScore userScore : userScoreList) {
            User user = userRepository.findById(userScore.getUserId()).orElseThrow(()-> new NoUserException("존재하지 않는 유저 아이디 findTop10Ranking"));
            UserScoreVO userScoreVO = new UserScoreVO();
            userScoreVO.setNickname(user.getNickname());
//            userScoreVO.setExerciseName(userScore.getExerciseName());
            userScoreVO.setScore(userScore.getScore());



            userScoreVOList.add(userScoreVO);
        }


        return ScoreServiceTop10Response.builder()
                .userScoreVOList(userScoreVOList)
                .build();
    }
}
