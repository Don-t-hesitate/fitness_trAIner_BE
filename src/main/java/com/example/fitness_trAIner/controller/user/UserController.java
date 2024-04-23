package com.example.fitness_trAIner.controller.user;

import com.example.fitness_trAIner.common.response.GlobalExceptionResponse;
import com.example.fitness_trAIner.common.response.GlobalResponse;
import com.example.fitness_trAIner.controller.user.dto.request.*;
import com.example.fitness_trAIner.service.user.UserService;
import com.example.fitness_trAIner.service.user.dto.request.*;
import com.example.fitness_trAIner.service.user.dto.response.UserServiceDetailInfoResponse;
import com.example.fitness_trAIner.service.user.dto.response.UserServiceLoginResponse;
import com.example.fitness_trAIner.service.user.dto.response.UserServiceSignupResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@Tag(name = "User", description = "유저 관련 API")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "유저 회원가입 API")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<UserServiceSignupResponse> saveUser(@RequestBody UserSignupRequestBody requestBody) {

        System.out.println(requestBody.getHeight());
        System.out.println(requestBody.getWeight());
        return GlobalResponse.<UserServiceSignupResponse>builder()
                .message("유저 회원가입")
                .result(userService.signupUser(UserServiceSignupRequest.builder()
                        .username(requestBody.getUsername())
                        .password(requestBody.getPassword())
                        .nickname(requestBody.getNickname())
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

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "유저 로그인 API")
    public final GlobalResponse<UserServiceLoginResponse> loginUser(@RequestBody UserLoginRequestBody requestBody) {

        return GlobalResponse.<UserServiceLoginResponse>builder()
                .message("유저 로그인")
                .result(userService.loginUser(UserServiceLoginRequest.builder()
                        .username(requestBody.getUsername())
                        .password(requestBody.getPassword())
                        .build()))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "아이디로 유저 조회", description = "유저 조회")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<UserServiceDetailInfoResponse> findById(@PathVariable Long id) {
        return GlobalResponse.<UserServiceDetailInfoResponse>builder()
                .message("유저 상세 조회")
                .result(userService.findById(id))
                .build();
    }

    @PutMapping()
    @Operation(summary = "사용자 정보 수정", description = "유저 조회")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> updateUser(@RequestBody UserUpdateRequestBody requestBody) {
        return GlobalResponse.<String>builder()
                .message("사용자 정보 수정")
                .result(userService.updateUser(UserServiceUpdateRequest.builder()
                        .id(requestBody.getId())
                        .nickname(requestBody.getNickname())
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
                .result(userService.deleteUser(id))
                .build();
    }

    @GetMapping("/{nickname}/{age}")
    @Operation(summary = "사용자 아이디 찾기", description = "사용자 닉네임/나이를 사용하여 아이디 찾기")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> findUsername(@PathVariable String requestNickname, @PathVariable Integer requestAge) {


        return GlobalResponse.<String>builder()
                .message("사용자 아이디 찾기")
                .result(userService.findUsername(UserServiceFindUsernameRequest.builder()
                        .nickname(requestNickname)
                        .age(requestAge)
                        .build()))
                .build();

    }

    @PatchMapping()
    @Operation(summary = "사용자 비밀번호 변경", description = "사용자 닉네임/아이디를 사용하여 비밀번호 변경")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> changePassword(UserChangePasswordRequestBody requestBody) {
        return GlobalResponse.<String>builder()
                .message("사용자 아이디 찾기")
                .result(userService.changePassword(UserServiceChangePasswordRequest.builder()
                        .nickname(requestBody.getNickname())
                        .username(requestBody.getUsername())
                        .newPassword(requestBody.getNewPassword())
                        .build()))
                .build();
    }

}
