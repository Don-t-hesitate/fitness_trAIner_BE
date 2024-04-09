package com.example.fitness_trAIner.service.user;

import com.example.fitness_trAIner.service.user.dto.request.UserServiceLoginRequest;
import com.example.fitness_trAIner.service.user.dto.request.UserServiceSignupRequest;
import com.example.fitness_trAIner.service.user.dto.response.UserServiceLoginResponse;
import com.example.fitness_trAIner.service.user.dto.response.UserServiceSignupResponse;

public interface UserService {
    public UserServiceSignupResponse signupUser(UserServiceSignupRequest request);
    public UserServiceLoginResponse loginUser(UserServiceLoginRequest request);

}
