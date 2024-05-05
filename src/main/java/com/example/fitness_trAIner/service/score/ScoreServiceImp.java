package com.example.fitness_trAIner.service.score;

import com.example.fitness_trAIner.common.exception.exceptions.ExerciseException;
import com.example.fitness_trAIner.common.exception.exceptions.NoUserException;
import com.example.fitness_trAIner.common.exception.exceptions.ScoreException;
import com.example.fitness_trAIner.repository.exercise.ExerciseRepository;
import com.example.fitness_trAIner.repository.user.User;
import com.example.fitness_trAIner.repository.user.UserRepository;
import com.example.fitness_trAIner.repository.user.UserScore;
import com.example.fitness_trAIner.repository.user.UserScoreRepository;
import com.example.fitness_trAIner.service.score.dto.response.ScoreServiceScoreListResponse;
import com.example.fitness_trAIner.service.score.dto.response.ScoreServiceTop10Response;
import com.example.fitness_trAIner.service.score.dto.response.ScoreServiceUserRankingResponse;
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
    private final ExerciseRepository exerciseRepository;
    @Override
    public ScoreServiceTop10Response findTop10Ranking(String exerciseName) {

        if (!userScoreRepository.existsByExerciseName(exerciseName)) {
            throw new ScoreException("점수가 존재하지 않는 운동");
        }

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
                .exerciseName(exerciseName)
                .userScoreVOList(userScoreVOList)
                .build();
    }

    @Override
    public ScoreServiceUserRankingResponse findUserRanking(String exerciseName, Long userId) {
        UserScore userScore = userScoreRepository.findByUserIdAndExerciseName(userId, exerciseName).orElseThrow(()-> new ScoreException("userId와 운동이름에 해당하는 점수 없음"));

        User user = userRepository.findById(userId).orElseThrow(()->new NoUserException("userId에 해당하는 유저 없음 findUserRanking"));

        Long userRanking = userScoreRepository.countByExerciseNameAndScoreGreaterThanEqual(exerciseName, userScore.getScore());



        return ScoreServiceUserRankingResponse.builder()
                .exerciseName(userScore.getExerciseName())
                .ranking(userRanking)
                .nickname(user.getNickname())
                .score(userScore.getScore())
                .build();
    }

    @Override
    public ScoreServiceScoreListResponse findScoreList() {
        List<String> scoreList = userScoreRepository.findDistinctExerciseName();


        return ScoreServiceScoreListResponse.builder()
                .scoreCategory(scoreList)
                .build();
    }


}
