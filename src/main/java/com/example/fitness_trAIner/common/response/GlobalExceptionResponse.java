package com.example.fitness_trAIner.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "Exception Response")
public class GlobalExceptionResponse<T> {
    @Schema(description = "API 호출 시 처리 성공 여부", example = "false")
    private boolean success = false;
    @Schema(description = "에러 발생 시 메시지", example = "에러 메시지")
    private T message;
    @Schema(description = "에러 발생 코드", example = "404")
    private int code;
    @Builder
    public GlobalExceptionResponse(T message, int code) {
        this.message = message;
        this.code = code;
    }
}
