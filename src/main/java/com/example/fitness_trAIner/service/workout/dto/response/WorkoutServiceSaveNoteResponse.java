package com.example.fitness_trAIner.service.workout.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutServiceSaveNoteResponse {
    private Long noteId;
}
