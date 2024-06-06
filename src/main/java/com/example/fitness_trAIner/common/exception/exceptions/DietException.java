package com.example.fitness_trAIner.common.exception.exceptions;

public class DietException extends RuntimeException {
    public DietException() {
        super("유효하지 않은 카테고리입니다.");
    }
    public DietException(String message) {
        super(message);
    }
}
