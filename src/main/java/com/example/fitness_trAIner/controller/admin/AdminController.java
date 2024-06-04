package com.example.fitness_trAIner.controller.admin;

import com.example.fitness_trAIner.common.response.GlobalExceptionResponse;
import com.example.fitness_trAIner.common.response.GlobalResponse;
import com.example.fitness_trAIner.controller.admin.dto.request.AdminLoginRequestBody;
import com.example.fitness_trAIner.controller.admin.dto.request.AdminUserPrefUpdateRequestBody;
import com.example.fitness_trAIner.controller.admin.dto.request.AdminUserUpdateRequestBody;
import com.example.fitness_trAIner.service.admin.AdminService;
import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceLoginRequest;
import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceUserPrefUpdateRequest;
import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceUserUpdateRequest;
import com.example.fitness_trAIner.service.admin.dto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RequestMapping("/admin")
@Tag(name = "Admin", description = "관리자 관련 API")
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;
    @PostMapping("/login")
    @Operation(summary = "관리자 로그인", description = "관리자 로그인 API")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = AdminServiceLoginResponse.class)))
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
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = AdminServiceFindUserListResponse.class)))
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<AdminServiceFindUserListResponse> findAllUserList() {


        return GlobalResponse.<AdminServiceFindUserListResponse>builder()
                .message("유저조회")
                .result(adminService.findUserList())
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "사용자 정보 수정", description = "사용자 정보 수정")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> updateUser(@RequestBody AdminUserUpdateRequestBody requestBody) {
        return GlobalResponse.<String>builder()
                .message("사용자 정보 수정")
                .result(adminService.adminUpdateUser(AdminServiceUserUpdateRequest.builder()
                        .userId(requestBody.getUserId())
                        .nickname(requestBody.getNickname())
                        .height(requestBody.getHeight())
                        .weight(requestBody.getWeight())
                        .age(requestBody.getAge())
                        .gender(requestBody.getGender())
                        .spicyPreference(requestBody.getSpicyPreference())
                        .meatConsumption(requestBody.getMeatConsumption())
                        .tastePreference(requestBody.getTastePreference())
                        .activityLevel(requestBody.getActivityLevel())
                        .preferenceTypeFood(requestBody.getPreferenceTypeFood())
                        .preferenceFoods(requestBody.getPreferenceFoods())
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

    @GetMapping("/food/preferences/{filePath}")
    @Operation(summary = "사용자 식단 선호도 조회", description = "모든 사용자 식단 선호도 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = List.class))
    )
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final ResponseEntity<Resource> getAllUserFoodPreferences(@PathVariable String filePath) throws IOException {
        byte[] bytes = adminService.getExcelFileBytes(filePath);
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(bytes));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=excel.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);

//        return GlobalResponse.<List<AdminServiceUserFoodPreferencesResponse>>builder()
//                .message("사용자 식단 선호도 조회")
//                .result(adminService.getExcelFileBytes(filePath))
//                .build();
    }

    @PutMapping(path = "/food/preferences/{filePath}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "사용자 식단 선호도 수정", description = "사용자 식단 선호도 수정")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<AdminServiceExcelSaveResponse> updateUserFoodPreferences(@RequestPart MultipartFile file, @PathVariable String filePath) {
        try {
            return GlobalResponse.<AdminServiceExcelSaveResponse>builder()
                    .message("엑셀 파일 업로드 성공")
                    .result(adminService.saveExcelData(file, filePath))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("엑셀 파일 업로드 중 오류 발생", e);
        }

//        return GlobalResponse.<String>builder()
//                .message("사용자 식단 선호도 수정")
//                .result(adminService.adminUpdateUserPref(AdminServiceUserPrefUpdateRequest.builder()
//                        .userId(requestBody.getUserId())
//                        .spicyPreference(requestBody.getSpicyPreference())
//                        .meatConsumption(requestBody.getMeatConsumption())
//                        .tastePreference(requestBody.getTastePreference())
//                        .activityLevel(requestBody.getActivityLevel())
//                        .preferenceTypeFood(requestBody.getPreferenceTypeFood())
//                        .build()))
//                .build();
    }

    // uri 수정 필요
    @GetMapping("/food/info/{filePath}")
    @Operation(summary = "엑셀 파일 전송", description = "엑셀 파일 전송")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Resource.class)))
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public ResponseEntity<Resource> getExcelFile(@PathVariable String filePath) throws IOException {
        byte[] bytes = adminService.getExcelFileBytes(filePath);
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(bytes));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=excel.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }

    @PostMapping(value = "/food/info/{filePath}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "엑셀 파일 저장", description = "웹에서 엑셀 파일 업로드")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public GlobalResponse<AdminServiceExcelSaveResponse> saveExcelData(@RequestPart MultipartFile file, @PathVariable String filePath) {
        try {
            return GlobalResponse.<AdminServiceExcelSaveResponse>builder()
                    .message("엑셀 파일 업로드 성공")
                    .result(adminService.saveExcelData(file, filePath))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("엑셀 파일 업로드 중 오류 발생", e);
        }
    }

    @GetMapping("/workouts/videos")
    @Operation(summary = "사용자 운동 영상 리스트 조회", description = "모든 사용자 운동 영상 정보 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = AdminServiceFindWorkoutVideoListResponse.class)))
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<AdminServiceFindWorkoutVideoListResponse> findAllWorkoutVideoList() {


        return GlobalResponse.<AdminServiceFindWorkoutVideoListResponse>builder()
                .message("유저조회")
                .result(adminService.findWorkoutVideoList())
                .build();
    }

    @DeleteMapping("/workouts/{wokroutVideoId}")
    @Operation(summary = "사용자 운동 영상 삭제", description = "사용자 운동영상 삭제")
    @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public final GlobalResponse<String> deleteWorkoutVideo(@PathVariable Long wokroutVideoId) {
        return GlobalResponse.<String>builder()
                .message("사용자 운동 영상 삭제")
                .result(adminService.deleteWorkoutVideo(wokroutVideoId))
                .build();
    }

    @GetMapping("/app")
    @Operation(summary = "앱 다운로드", description = "앱 다운로드")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Resource.class)))
    @ApiResponse(responseCode = "400", description = "에러 발생", content = @Content(schema = @Schema(implementation = GlobalExceptionResponse.class)))
    public ResponseEntity<Resource> getApp() throws IOException {
        byte[] bytes = adminService.getApp();
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(bytes));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=trAIner.apk");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("application/vnd.android.package-archive"))
                .body(resource);
    }
}
