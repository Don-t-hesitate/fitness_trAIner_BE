package com.example.fitness_trAIner.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    AUTHENTICATION_ERROR(301, "인증 에러"),
    FILESTORE_ERROR(305, "파일 저장 실패 오류"),
    NOTESAVE_ERROR(302, "일지 저장 실패 오류"),
    AI_ERROR(303, "AI 오류"),
    UNAUTHORIZED(304, "권한이 없음 오류"),
    DIET_ERROR(305, "식단 업로드 오류"),
    ;


    private int code;
    private String message;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
