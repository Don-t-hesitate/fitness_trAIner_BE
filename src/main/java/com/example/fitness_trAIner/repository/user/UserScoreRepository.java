package com.example.fitness_trAIner.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserScoreRepository extends JpaRepository<UserScore, Long> {
    Optional<UserScore> findByUserIdAndExerciseName(Long userId, String exerciseName);
    boolean existsByUserIdAndExerciseName(Long userId, String exerciseName);

    List<UserScore> findTop10ByExerciseNameOrderByScoreDesc(String exerciseName);

    Long countByExerciseNameAndScoreGreaterThanEqual(String exerciseName, int score);
}
