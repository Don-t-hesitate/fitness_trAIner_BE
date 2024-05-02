package com.example.fitness_trAIner.repository.exercise;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@Table(name = "exercise")
public class Exercise {
    @Id
    @Column(name = "exercise_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exerciseId;
    @Column(name = "exercise_name")
    private String exerciseName;
    @Column(name = "per_kcal")
    private int perKcal;


}
