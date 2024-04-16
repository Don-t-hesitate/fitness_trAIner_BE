package com.example.fitness_trAIner.service.admin;

import com.example.fitness_trAIner.common.exception.exceptions.LoginFailException;
import com.example.fitness_trAIner.common.exception.exceptions.NoUserException;
import com.example.fitness_trAIner.common.exception.exceptions.RoleAccessDeniedException;
import com.example.fitness_trAIner.common.exception.exceptions.SignupFailException;
import com.example.fitness_trAIner.repository.user.User;
import com.example.fitness_trAIner.repository.user.UserRepository;
import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceLoginRequest;
import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceUserUpdateRequest;
import com.example.fitness_trAIner.service.admin.dto.response.AdminServiceFindUserListResponse;
import com.example.fitness_trAIner.service.admin.dto.response.AdminServiceLoginResponse;
import com.example.fitness_trAIner.service.user.dto.request.UserServiceUpdateRequest;
import com.example.fitness_trAIner.vos.UserVO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class AdminServiceImp implements AdminService{


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public AdminServiceLoginResponse loginAdmin(AdminServiceLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(()-> new LoginFailException("아이디 없음"));

        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new LoginFailException("비밀번호 불일치");
        }
        if(!user.getRole().equals("ADMIN")) {
            throw new RoleAccessDeniedException("권한없음 loginAdmin");
        }

        return AdminServiceLoginResponse.builder().id(user.getId()).build();
    }

    @Override
    public AdminServiceFindUserListResponse findUserList() {
        List<User> userList = userRepository.findAll();
        List<UserVO> userVOList = new ArrayList<>();

        for (User user : userList) {
            UserVO userVO = new UserVO();
            userVO.setUserId(user.getId());
            userVO.setUsername(user.getUsername());
            userVO.setNickname(user.getNickname());

            userVOList.add(userVO);
        }

        return AdminServiceFindUserListResponse.builder()
                .userList(userVOList)
                .build();
    }

    @Override
    public String adminUpdateUser(AdminServiceUserUpdateRequest request) {
        User user = userRepository.findById(request.getId()).orElseThrow(()->new NoUserException("유저 조회 오류 updateUser"));

        Boolean isExistNickname = userRepository.existsByNickname(request.getNickname());
        if (isExistNickname) throw new SignupFailException("닉네임 중복");

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


}
