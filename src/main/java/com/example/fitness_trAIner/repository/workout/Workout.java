package com.example.fitness_trAIner.repository.workout;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@Table(name = "workout")
public class Workout {

    @Id
    @Column(name = "workout_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workoutId;

    @Column(name = "note_id", nullable = false)
    private Long noteId;
    @Column(name = "exercise_name")
    private String exerciseName;
    @Column(name = "set_num")
    private Integer setNum;
    @Column(name = "repeats")
    private Integer repeats;



}
