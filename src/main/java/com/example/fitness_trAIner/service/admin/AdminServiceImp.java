package com.example.fitness_trAIner.service.admin;

import com.example.fitness_trAIner.common.exception.exceptions.*;
import com.example.fitness_trAIner.repository.user.User;
import com.example.fitness_trAIner.repository.user.UserRepository;
import com.example.fitness_trAIner.repository.workout.WorkoutVideo;
import com.example.fitness_trAIner.repository.workout.WorkoutVideoRepository;
import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceLoginRequest;
import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceUserUpdateRequest;
import com.example.fitness_trAIner.service.admin.dto.response.AdminServiceFindUserListResponse;
import com.example.fitness_trAIner.service.admin.dto.response.AdminServiceFindWorkoutVideoListResponse;
import com.example.fitness_trAIner.service.admin.dto.response.AdminServiceLoginResponse;
import com.example.fitness_trAIner.service.user.dto.request.UserServiceUpdateRequest;
import com.example.fitness_trAIner.vos.UserVO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class AdminServiceImp implements AdminService{


    private final UserRepository userRepository;
    private final WorkoutVideoRepository workoutVideoRepository;
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

        return AdminServiceLoginResponse.builder().id(user.getUserId()).build();
    }

    @Override
    public AdminServiceFindUserListResponse findUserList() {
        List<User> userList = userRepository.findAll();
        List<UserVO> userVOList = new ArrayList<>();

        for (User user : userList) {
            UserVO userVO = new UserVO();
            userVO.setUserId(user.getUserId());
            userVO.setUsername(user.getUsername());
            userVO.setNickname(user.getNickname());
            userVO.setAge(user.getAge());
            userVO.setHeight(user.getHeight());
            userVO.setWeight(user.getWeight());



            userVOList.add(userVO);
        }

        return AdminServiceFindUserListResponse.builder()
                .userList(userVOList)
                .build();
    }

    @Override
    public String adminUpdateUser(AdminServiceUserUpdateRequest request) {
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
    public String deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new NoUserException("유저 조회 오류 deleteUser"));

        userRepository.deleteById(user.getUserId());
        return "사용자 탈퇴 성공";
    }

    @Override
    public AdminServiceFindWorkoutVideoListResponse findWorkoutVideoList() {
        List<WorkoutVideo> workoutVideoList = workoutVideoRepository.findAll();

        return AdminServiceFindWorkoutVideoListResponse.builder()
                .workoutVideoList(workoutVideoList)
                .build();
    }

    @Override
    public String deleteWorkoutVideo(Long workoutVideoId) {
        WorkoutVideo workoutVideo = workoutVideoRepository.findById(workoutVideoId).orElseThrow(()->new FileStoreException("동영상 없음"));

        File file = new File(workoutVideo.getFileName());
        try{
            file.delete();
        }catch (Exception e){
            throw new FileStoreException("영상 삭제 실패");
        }


        workoutVideoRepository.deleteById(workoutVideo.getWorkoutVideoId());

        return "영상 삭제 성공";
    }

}
