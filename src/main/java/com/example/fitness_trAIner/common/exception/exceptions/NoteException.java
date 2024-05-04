package com.example.fitness_trAIner.common.exception.exceptions;

public class NoteException extends RuntimeException{
    public NoteException() {
        super("노트 관련 오류");
    }
    public NoteException(String message) {
        super(message);
    }
}
