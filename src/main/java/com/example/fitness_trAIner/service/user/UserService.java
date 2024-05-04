package com.example.fitness_trAIner.service.user;

import com.example.fitness_trAIner.service.user.dto.request.*;
import com.example.fitness_trAIner.service.user.dto.response.UserServiceDetailInfoResponse;
import com.example.fitness_trAIner.service.user.dto.response.UserServiceLoginResponse;
import com.example.fitness_trAIner.service.user.dto.response.UserServiceSignupResponse;

public interface UserService {
    public UserServiceSignupResponse signupUser(UserServiceSignupRequest request);
    public UserServiceLoginResponse loginUser(UserServiceLoginRequest request);
    public UserServiceDetailInfoResponse findById(Long id);
    public String updateUser(UserServiceUpdateRequest request);
    public String deleteUser(Long id);
    public String findUsername(UserServiceFindUsernameRequest request);
    public String changePassword(UserServiceChangePasswordRequest request);
    public void resetAttendance();

}
