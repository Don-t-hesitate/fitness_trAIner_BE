package com.example.fitness_trAIner.service.admin;

import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceLoginRequest;
import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceUserPrefUpdateRequest;
import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceUserUpdateRequest;
import com.example.fitness_trAIner.service.admin.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdminService {

    public AdminServiceLoginResponse loginAdmin(AdminServiceLoginRequest request);
    public AdminServiceFindUserListResponse findUserList();
    public String adminUpdateUser(AdminServiceUserUpdateRequest request);
    public String deleteUser(Long id);
    public String adminUpdateUserPref(AdminServiceUserPrefUpdateRequest request);
    public List<AdminServiceUserFoodPreferencesResponse> getAllUserFoodPreferences();
    byte[] getExcelFileBytes(String filePath) throws IOException;
    public AdminServiceExcelSaveResponse saveExcelData(MultipartFile file, String filePath) throws IOException;

    public AdminServiceFindWorkoutVideoListResponse findWorkoutVideoList();
    public String deleteWorkoutVideo(Long wokroutVideoId);

}
