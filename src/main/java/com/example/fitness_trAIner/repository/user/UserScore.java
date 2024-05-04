package com.example.fitness_trAIner.repository.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Builder
@AllArgsConstructor
@Table(name = "user_score")
public class UserScore {

    @Id
    @Column(name = "score_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scoreId;

    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "exercise_name")
    private String exerciseName;
    @Column(name = "score")
    private int score;

}
