package com.example.fitness_trAIner.common.exception.exceptions;

public class NoUserException extends RuntimeException{
    public NoUserException() {
        super("사용자가 존재하지 않음");
    }
    public NoUserException(String message) {
        super(message);
    }
}
