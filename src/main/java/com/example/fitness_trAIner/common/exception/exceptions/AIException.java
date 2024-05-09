package com.example.fitness_trAIner.common.exception.exceptions;

public class AIException extends RuntimeException{
    public AIException() {
        super("AI관련 오류");
    }
    public AIException(String message) {
        super(message);
    }
}
