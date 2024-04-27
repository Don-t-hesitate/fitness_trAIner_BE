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
    private Integer totalScore;
    @Column(name = "total_kcal")
    private Integer totalKcal;
    @Column(name = "total_set")
    private Integer totalSet;
    @Column(name = "score_cool")
    private Integer scoreCool;
    @Column(name = "score_good")
    private Integer scoreGood;
    @Column(name = "score_notgood")
    private Integer scoreNotgood;



}


