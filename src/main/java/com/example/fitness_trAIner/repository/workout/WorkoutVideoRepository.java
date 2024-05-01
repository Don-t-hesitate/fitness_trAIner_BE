package com.example.fitness_trAIner.repository.workout;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutVideoRepository extends JpaRepository<WorkoutVideo, Long> {
    WorkoutVideo findByWorkoutVideoId(Long workoutVideoId);

}
