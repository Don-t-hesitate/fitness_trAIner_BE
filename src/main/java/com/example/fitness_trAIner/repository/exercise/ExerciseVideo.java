package com.example.fitness_trAIner.repository.exercise;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "exercise_video")
public class ExerciseVideo {
    @Id
    @Column(name = "exercise_video_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exerciseVideoId;
    @Column(name = "exercise_name")
    private String exerciseName;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;
}
