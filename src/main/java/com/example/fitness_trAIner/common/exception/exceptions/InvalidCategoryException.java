package com.example.fitness_trAIner.common.exception.exceptions;

public class InvalidCategoryException extends RuntimeException {
    public InvalidCategoryException() {
        super("유효하지 않은 카테고리입니다.");
    }
    public InvalidCategoryException(String message) {
        super(message);
    }
}
