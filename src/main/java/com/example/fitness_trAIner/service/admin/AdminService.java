package com.example.fitness_trAIner.service.admin;

import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceLoginRequest;
import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceUserUpdateRequest;
import com.example.fitness_trAIner.service.admin.dto.response.AdminServiceFindUserListResponse;
import com.example.fitness_trAIner.service.admin.dto.response.AdminServiceFindWorkoutVideoListResponse;
import com.example.fitness_trAIner.service.admin.dto.response.AdminServiceLoginResponse;
import com.example.fitness_trAIner.service.user.dto.request.UserServiceUpdateRequest;

public interface AdminService {

    public AdminServiceLoginResponse loginAdmin(AdminServiceLoginRequest request);
    public AdminServiceFindUserListResponse findUserList();
    public String adminUpdateUser(AdminServiceUserUpdateRequest request);
    public String deleteUser(Long id);

    public AdminServiceFindWorkoutVideoListResponse findWorkoutVideoList();
    public String deleteWorkoutVideo(Long wokroutVideoId);

}
