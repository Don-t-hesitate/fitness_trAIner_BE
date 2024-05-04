package com.example.fitness_trAIner.repository.exercise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findByExerciseName(String exerciseName);
    Optional<Exercise> findByExerciseId(Long exerciseId);
    boolean existsByExerciseName(String exerciseName);
    void deleteByExerciseName(String exerciseName);

}
