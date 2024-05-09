package com.example.fitness_trAIner.service.admin.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminServiceExcelSaveResponse {
    private String fileName;
    private String fileType;
    private long size;
}
