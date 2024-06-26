package com.example.fitness_trAIner.common.exception;


import com.example.fitness_trAIner.common.exception.exceptions.*;
import com.example.fitness_trAIner.common.response.GlobalExceptionResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

//Exception 전역 관리
@RestControllerAdvice
@Hidden
@Slf4j
public class ExceptionController {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final GlobalExceptionResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error("잘못된 RequestBody", e);
        return makeResponse(e.getBindingResult()
                .getFieldErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList()));
    }

    @ExceptionHandler({SignupFailException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final GlobalExceptionResponse handleSignupFailException(final SignupFailException e) {
        log.error("회원가입 오류", e);
        return makeResponse(e.getMessage(), ErrorCode.AUTHENTICATION_ERROR.getCode());
    }

    @ExceptionHandler({LoginFailException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final GlobalExceptionResponse loginFailException(final LoginFailException e) {
        log.error("로그인 오류", e);
        return makeResponse(e.getMessage(), ErrorCode.AUTHENTICATION_ERROR.getCode());
    }

    @ExceptionHandler({NoUserException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final GlobalExceptionResponse noUserCaseException(final NoUserException e) {
        log.error("사용자를 찾을 수 없음 오류", e);
        return makeResponse(e.getMessage(), ErrorCode.AUTHENTICATION_ERROR.getCode());
    }

    @ExceptionHandler({RoleAccessDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public final GlobalExceptionResponse roleAccessDeniedException(final RoleAccessDeniedException e) {
        log.error("권한이 없음 오류", e);
        return makeResponse(e.getMessage(), ErrorCode.UNAUTHORIZED.getCode());
    }


    @ExceptionHandler({FileStoreException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public final GlobalExceptionResponse fileStoreFailException(final FileStoreException e) {
        log.error(ErrorCode.FILESTORE_ERROR.getMessage(), e);
        return makeResponse(e.getMessage(), ErrorCode.FILESTORE_ERROR.getCode());
    }

    @ExceptionHandler({DietException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final GlobalExceptionResponse dietException(final DietException e) {
        log.error("식단 관련 오류", e);
        return makeResponse(e.getMessage(), ErrorCode.DIET_ERROR.getCode());
    }

    @ExceptionHandler({NoteException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final GlobalExceptionResponse noteSaveException(final NoteException e) {
        log.error("노트 관련 오류", e);
        return makeResponse(e.getMessage(), ErrorCode.NOTESAVE_ERROR.getCode());
    }

    @ExceptionHandler({ScoreException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final GlobalExceptionResponse scoreException(final ScoreException e) {
        log.error("점수 관련 오류", e);
        return makeResponse(e.getMessage(), ErrorCode.SCORE_ERROR.getCode());
    }

    @ExceptionHandler({ExerciseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final GlobalExceptionResponse exerciseException(final ExerciseException e) {
        log.error("예시 운동 관련 오류", e);
        return makeResponse(e.getMessage(), ErrorCode.EXERCISE_ERROR.getCode());
    }
    @ExceptionHandler({AIException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final GlobalExceptionResponse aiException(final AIException e) {
        log.error("AI관련", e);
        return makeResponse(e.getMessage(), ErrorCode.AI_ERROR.getCode());
    }

    @ExceptionHandler({EmptyDirectoryException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final GlobalExceptionResponse emptyDirectoryException(final EmptyDirectoryException e) {
        log.error("파일 조회 관련 오류", e);
        return makeResponse(e.getMessage(), ErrorCode.DIRECTORY_ERROR.getCode());
    }





    private GlobalExceptionResponse<String> makeResponse(String message, int code) {
        return GlobalExceptionResponse.<String>builder()
                .message(message)
                .code(code)
                .build();
    }

    private GlobalExceptionResponse<List<String>> makeResponse(List<String> message) {
        return GlobalExceptionResponse.<List<String>>builder()
                .message(message)
                .build();
    }


}
