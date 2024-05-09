package com.example.fitness_trAIner.controller.admin.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class AdminExcelSaveRequestBody {
    private MultipartFile file;
}
