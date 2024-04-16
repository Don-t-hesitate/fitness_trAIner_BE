package com.example.fitness_trAIner.service.admin;

import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceLoginRequest;
import com.example.fitness_trAIner.service.admin.dto.response.AdminServiceFindUserListResponse;
import com.example.fitness_trAIner.service.admin.dto.response.AdminServiceLoginResponse;

public interface AdminService {

    public AdminServiceLoginResponse loginAdmin(AdminServiceLoginRequest request);
    public AdminServiceFindUserListResponse findUserList();


}
