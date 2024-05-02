package com.example.fitness_trAIner.repository.workout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Note save(Long id);
    List<Note> findAllByUserIdAndWorkoutDate(Long userId, LocalDate workoutDate);
    boolean existsByUserId(Long userId);
    boolean existsByNoteId(Long noteId);
    Note findByNoteId(Long noteId);
}
