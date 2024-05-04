package com.example.fitness_trAIner.service.workout.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutServiceFindNoteListRequest {
    private Long userId;
    private LocalDate date;
}
