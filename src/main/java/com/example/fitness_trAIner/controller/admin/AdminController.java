package com.example.fitness_trAIner.controller.admin;

import com.example.fitness_trAIner.common.response.GlobalExceptionResponse;
import com.example.fitness_trAIner.common.response.GlobalResponse;
import com.example.fitness_trAIner.controller.admin.dto.request.AdminLoginRequestBody;
import com.example.fitness_trAIner.controller.admin.dto.request.AdminUserUpdateRequestBody;
import com.example.fitness_trAIner.controller.user.dto.request.UserSignupRequestBody;
import com.example.fitness_trAIner.controller.user.dto.request.UserUpdateRequestBody;
import com.example.fitness_trAIner.service.admin.AdminService;
import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceLoginRequest;
import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceUserUpdateRequest;
import com.example.fitness_trAIner.service.admin.dto.response.AdminServiceFindUserListResponse;
import com.example.fitness_trAIner.service.admin.dto.response.AdminServiceLoginResponse;
import com.example.fitness_trAIner.service.user.dto.request.UserServiceSignupRequest;
import com.example.fitness_trAIner.service.user.dto.request.UserServiceUpdateRequest;
import com.example.fitness_trAIner.service.user.dto.response.UserServiceSignupResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/users")
    @Operation(summary = "사용자 조회", description = "모든 사용자 조회")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<AdminServiceFindUserListResponse> findAllUserList() {


        return GlobalResponse.<AdminServiceFindUserListResponse>builder()
                .message("유저조회")
                .result(adminService.findUserList())
                .build();
    }

    @PutMapping()
    @Operation(summary = "사용자 정보 수정", description = "사용자 정보 수정")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> updateUser(@RequestBody AdminUserUpdateRequestBody requestBody) {
        return GlobalResponse.<String>builder()
                .message("사용자 정보 수정")
                .result(adminService.adminUpdateUser(AdminServiceUserUpdateRequest.builder()
                        .id(requestBody.getId())
//                        .nickname(requestBody.getNickname())
                        .height(requestBody.getHeight())
                        .weight(requestBody.getWeight())
                        .age(requestBody.getAge())
                        .spicyPreference(requestBody.getSpicyPreference())
                        .meatConsumption(requestBody.getMeatConsumption())
                        .tastePreference(requestBody.getTastePreference())
                        .activityLevel(requestBody.getActivityLevel())
                        .preferenceTypeFood(requestBody.getPreferenceTypeFood())
                        .build()))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "사용자 탈퇴", description = "사용자 탈퇴")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> deleteUser(@PathVariable Long id) {
        return GlobalResponse.<String>builder()
                .message("사용자 탈퇴")
                .result(adminService.deleteUser(id))
                .build();
    }

}
