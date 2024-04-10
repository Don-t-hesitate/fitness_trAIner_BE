package com.example.fitness_trAIner.controller.admin;

import com.example.fitness_trAIner.common.response.GlobalExceptionResponse;
import com.example.fitness_trAIner.common.response.GlobalResponse;
import com.example.fitness_trAIner.controller.admin.dto.request.AdminLoginRequestBody;
import com.example.fitness_trAIner.controller.user.dto.request.UserSignupRequestBody;
import com.example.fitness_trAIner.service.admin.AdminService;
import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceLoginRequest;
import com.example.fitness_trAIner.service.admin.dto.response.AdminServiceLoginResponse;
import com.example.fitness_trAIner.service.user.dto.request.UserServiceSignupRequest;
import com.example.fitness_trAIner.service.user.dto.response.UserServiceSignupResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@Tag(name = "Admin", description = "관리자 관련 API")
@RequiredArgsConstructor
@RestController
public class AdminController {
    private final AdminService adminService;
    @PostMapping("/login")
    @Operation(summary = "관리자 로그인", description = "관리자 로그인 API")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<AdminServiceLoginResponse> loginAdmin(@RequestBody AdminLoginRequestBody requestBody) {


        return GlobalResponse.<AdminServiceLoginResponse>builder()
                .message("관리자 로그인")
                .result(adminService.loginAdmin(AdminServiceLoginRequest.builder()
                                .username(requestBody.getUsername())
                                .password(requestBody.getPassword())
                        .build()))
                .build();
    }
}
