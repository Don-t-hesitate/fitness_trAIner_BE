package com.example.fitness_trAIner.repository.workout;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

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
@Table(name = "workout_video")
public class WorkoutVideo {
    @Id
    @Column(name = "workout_video_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workoutVideoId;

    @Column(name = "note_id", nullable = false)
    private Long noteId;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "exercise_name")
    private String exerciseName;
    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

}
