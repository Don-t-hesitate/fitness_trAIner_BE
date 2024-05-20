package com.example.fitness_trAIner.repository.diet;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@Table(name = "diet")
public class Diet {

    @Id
    @Column(name = "diet_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dietId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "food_id", nullable = false)
    private Long foodId;

    @Column(name = "eat_date", nullable = false)
    private LocalDate eatDate;

    @Column(name = "total_calories", nullable = false)
    private Double totalCalories;

}
