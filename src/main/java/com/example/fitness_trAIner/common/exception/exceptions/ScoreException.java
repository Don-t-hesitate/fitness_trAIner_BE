package com.example.fitness_trAIner.common.exception.exceptions;

public class ScoreException extends RuntimeException{
    public ScoreException() {
        super("점수 오류");
    }
    public ScoreException(String message) {
        super(message);
    }
}
