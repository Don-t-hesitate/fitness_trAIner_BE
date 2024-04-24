package com.example.fitness_trAIner.service.user;

import com.example.fitness_trAIner.common.exception.exceptions.LoginFailException;
import com.example.fitness_trAIner.common.exception.exceptions.NoUserException;
import com.example.fitness_trAIner.common.exception.exceptions.SignupFailException;
import com.example.fitness_trAIner.repository.user.User;
import com.example.fitness_trAIner.repository.user.UserRepository;
import com.example.fitness_trAIner.service.user.dto.request.*;
import com.example.fitness_trAIner.service.user.dto.response.UserServiceDetailInfoResponse;
import com.example.fitness_trAIner.service.user.dto.response.UserServiceLoginResponse;
import com.example.fitness_trAIner.service.user.dto.response.UserServiceSignupResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserServiceSignupResponse signupUser(UserServiceSignupRequest request) {

        Boolean isExistUsername = userRepository.existsByUsername(request.getUsername());
        Boolean isExistNickname = userRepository.existsByNickname(request.getNickname());

        //중복검사
        if (isExistUsername) throw new SignupFailException("아이디 중복");
        if (isExistNickname) throw new SignupFailException("닉네임 중복");

        User user;
        
        try {
            user = userRepository.save(User.builder()
                    .username(request.getUsername())
                    .password(bCryptPasswordEncoder.encode(request.getPassword()))
                    .nickname(request.getNickname())
                    .height(request.getHeight())
                    .weight(request.getWeight())
                    .age(request.getAge())
                    .spicyPreference(request.getSpicyPreference())
                    .meatConsumption(request.getMeatConsumption())
                    .tastePreference(request.getTastePreference())
                    .activityLevel(request.getActivityLevel())
                    .preferenceTypeFood(request.getPreferenceTypeFood())
                    .role("NORMAL")
                    .build());
        } catch (Exception e) {
            throw new SignupFailException("db저장 실패");
        }
        return UserServiceSignupResponse.builder()
                .name(user.getUsername())
                .password(user.getPassword())
                .build();


    }

    @Override
    public UserServiceLoginResponse loginUser(UserServiceLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(()-> new LoginFailException("아이디 없음 loginUser"));

        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword()))
                throw new LoginFailException("비밀번호 불일치");

        return UserServiceLoginResponse.builder().id(user.getUserId()).build();
    }

    @Override
    public UserServiceDetailInfoResponse findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new NoUserException("유저 조회 오류 findById"));


        return UserServiceDetailInfoResponse.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .height(user.getHeight())
                .weight(user.getWeight())
                .age(user.getAge())
                .spicyPreference(user.getSpicyPreference())
                .meatConsumption(user.getMeatConsumption())
                .tastePreference(user.getTastePreference())
                .activityLevel(user.getActivityLevel())
                .preferenceTypeFood(user.getPreferenceTypeFood())
                .build();
    }

    @Override
    @Transactional
    public String updateUser(UserServiceUpdateRequest request) {
        User user = userRepository.findById(request.getId()).orElseThrow(()->new NoUserException("유저 조회 오류 updateUser"));

        if (!request.getNickname().equals(user.getNickname())) {
            Boolean isExistNickname = userRepository.existsByNickname(request.getNickname());
            if (isExistNickname) throw new SignupFailException("닉네임 중복");
        }

        user.setNickname(request.getNickname());
        user.setHeight(request.getHeight());
        user.setWeight(request.getWeight());
        user.setAge(request.getAge());
        user.setSpicyPreference(request.getSpicyPreference());
        user.setMeatConsumption(request.getMeatConsumption());
        user.setTastePreference(request.getTastePreference());
        user.setActivityLevel(request.getActivityLevel());
        user.setPreferenceTypeFood(request.getPreferenceTypeFood());

        userRepository.save(user);



        return "사용자 정보 수정 성공";
    }

    @Override
    @Transactional
    public String deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new NoUserException("유저 조회 오류 deleteUser"));

        userRepository.deleteById(user.getUserId());
        return "사용자 탈퇴 성공";
    }

    @Override
    public String findUsername(UserServiceFindUsernameRequest request) {
        User user = userRepository.findByNicknameAndAge(request.getNickname(), request.getAge()).orElseThrow(()->new NoUserException("유저 조회 오류 findUsername"));

        return user.getUsername();
    }

    @Override
    @Transactional
    public String changePassword(UserServiceChangePasswordRequest request) {
        User user = userRepository.findByUsernameAndNickname(request.getUsername(), request.getNickname()).orElseThrow(()->new NoUserException("유저 조회 오류 changePassword"));

        user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));

        return "비밀번호 변경 성공";
    }


}
