package com.example.fitness_trAIner.common.exception.exceptions;

import org.springframework.security.access.AccessDeniedException;

public class RoleAccessDeniedException extends AccessDeniedException {
    public RoleAccessDeniedException() {
        super("권한없음");
    }
    public RoleAccessDeniedException(String message) {
        super(message);
    }
}
