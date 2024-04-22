package com.example.fitness_trAIner.common.exception.exceptions;

public class FileStoreException extends RuntimeException{
    public FileStoreException() {
        super("파일 저장 실패");
    }
    public FileStoreException(String message) {
        super(message);
    }
}
