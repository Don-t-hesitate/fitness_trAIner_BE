package com.example.fitness_trAIner.repository.workout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutVideoRepository extends JpaRepository<WorkoutVideo, Long> {
    WorkoutVideo findByWorkoutVideoId(Long workoutVideoId);
    List<WorkoutVideo> findAllByNoteId(Long noteId);

}
