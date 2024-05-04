package com.example.fitness_trAIner.common.exception.exceptions;

public class ExerciseException extends RuntimeException{
    public ExerciseException() {
        super("운동 예시 오류");
    }
    public ExerciseException(String message) {
        super(message);
    }
}
