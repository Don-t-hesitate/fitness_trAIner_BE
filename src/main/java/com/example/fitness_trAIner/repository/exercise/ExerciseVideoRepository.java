package com.example.fitness_trAIner.repository.exercise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface ExerciseVideoRepository extends JpaRepository<ExerciseVideo, Long> {
    ExerciseVideo findByExerciseVideoId(Long id);
    Optional<ExerciseVideo> findByExerciseName(String exerciseName);
    boolean existsByExerciseName(String exerciseName);
}
