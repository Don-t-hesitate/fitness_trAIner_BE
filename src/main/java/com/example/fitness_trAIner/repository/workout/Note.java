package com.example.fitness_trAIner.repository.workout;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Date;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "note")
public class Note {
    @Id
    @Column(name = "note_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;

    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "workout_date")
    @CreatedDate
    private LocalDate workoutDate;
    @Column(name = "total_score")
    private int totalScore;
    @Column(name = "total_kcal")
    private int totalKcal;
    @Column(name = "total_set")
    private int totalSet;
    @Column(name = "total_perfect")
    private int totalPerfect;
    @Column(name = "total_good")
    private int totalGood;
    @Column(name = "total_bad")
    private int totalBad;



}


