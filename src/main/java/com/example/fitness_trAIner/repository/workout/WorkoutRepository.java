package com.example.fitness_trAIner.repository.workout;

import com.example.fitness_trAIner.vos.WorkoutVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    Workout save(Workout workout);
    List<Workout> findAllByNoteId(Long noteId);
}
