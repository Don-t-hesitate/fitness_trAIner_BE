package com.example.fitness_trAIner.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(name = "전체 Response Wrapper")
public class GlobalResponse<T> {
    @Schema(description = "API 호출 성공 여부", example = "true")
    private final boolean success = true;
    @Schema(description = "API 호출 메시지", example ="{URL명} API 호출 성공")
    private String message;

    private T result;
    @Builder
    private GlobalResponse(String message, T result) {
        this.message = message;
        this.result = result;
    }
}
