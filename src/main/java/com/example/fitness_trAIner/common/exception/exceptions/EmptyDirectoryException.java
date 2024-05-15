package com.example.fitness_trAIner.common.exception.exceptions;

public class EmptyDirectoryException extends RuntimeException {
    public EmptyDirectoryException() {
        super();
    }

    public EmptyDirectoryException(String message) {
        super(message);
    }

    public EmptyDirectoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyDirectoryException(Throwable cause) {
        super(cause);
    }
}
