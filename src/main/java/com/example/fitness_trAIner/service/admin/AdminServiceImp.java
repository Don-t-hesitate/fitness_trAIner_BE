package com.example.fitness_trAIner.service.admin;

import com.example.fitness_trAIner.common.exception.exceptions.*;
import com.example.fitness_trAIner.repository.user.User;
import com.example.fitness_trAIner.repository.user.UserRepository;
import com.example.fitness_trAIner.repository.workout.WorkoutVideo;
import com.example.fitness_trAIner.repository.workout.WorkoutVideoRepository;
import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceLoginRequest;
import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceUserPrefUpdateRequest;
import com.example.fitness_trAIner.service.admin.dto.request.AdminServiceUserUpdateRequest;
import com.example.fitness_trAIner.service.admin.dto.response.*;
import com.example.fitness_trAIner.vos.UserVO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            userVO.setGender(user.getGender());



            userVOList.add(userVO);
        }

        return AdminServiceFindUserListResponse.builder()
                .userList(userVOList)
                .build();
    }

    @Override
    public String adminUpdateUser(AdminServiceUserUpdateRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(()->new NoUserException("유저 조회 오류 updateUser"));

        if (!request.getNickname().equals(user.getNickname())) {
            Boolean isExistNickname = userRepository.existsByNickname(request.getNickname());
            if (isExistNickname) throw new SignupFailException("닉네임 중복");
        }


        user.setNickname(request.getNickname());
        user.setHeight(request.getHeight());
        user.setWeight(request.getWeight());
        user.setAge(request.getAge());
        user.setGender(request.getGender());
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
    public String adminUpdateUserPref(AdminServiceUserPrefUpdateRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(()->new NoUserException("유저 조회 오류 updateUser"));

        user.setSpicyPreference(request.getSpicyPreference());
        user.setMeatConsumption(request.getMeatConsumption());
        user.setTastePreference(request.getTastePreference());
        user.setActivityLevel(request.getActivityLevel());
        user.setPreferenceTypeFood(request.getPreferenceTypeFood());

        userRepository.save(user);

        return "사용자 선호도 정보 수정 성공";
    }

    public List<AdminServiceUserFoodPreferencesResponse> getAllUserFoodPreferences() {
        List<User> userList = userRepository.findAll();
        List<AdminServiceUserFoodPreferencesResponse> adminServiceUserFoodPreferencesResponseList = new ArrayList<>();

        for (User user : userList) {
            AdminServiceUserFoodPreferencesResponse adminServiceUserFoodPreferencesResponse = AdminServiceUserFoodPreferencesResponse.builder()
                    .userId(user.getUserId())
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                    .spicyPreference(user.getSpicyPreference())
                    .meatConsumption(user.getMeatConsumption())
                    .tastePreference(user.getTastePreference())
                    .activityLevel(user.getActivityLevel())
                    .preferenceTypeFood(user.getPreferenceTypeFood())
                    .build();

            adminServiceUserFoodPreferencesResponseList.add(adminServiceUserFoodPreferencesResponse);
        }

        return adminServiceUserFoodPreferencesResponseList;
    }

    @Override
    public byte[] getExcelFileBytes() throws IOException {
        // jar 파일로 만들 시 참조 위치 재조정 필요
//        ClassPathResource classPathResource = new ClassPathResource("food_db_result_final.xlsx");
//        InputStream inputStream = classPathResource.getInputStream();
        String filePath = "/home/t24108/aidata/excel/food_db_result_final.xlsx";

        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        return IOUtils.toByteArray(inputStream);
    }

    @Override
    public AdminServiceExcelSaveResponse saveExcelData(MultipartFile file) throws IOException {
        // jar 파일로 만들 시 참조 위치 재조정 필요
//        String filePath = "D:/temp.xlsx";
        String filePath = "/home/t24108/aidata/excel/food_db_result_final.xlsx";
        File dest = new File(filePath);

        // 기존 파일이 있으면 .bak 확장자로 백업
        if (dest.exists()) {
            String backupFilePath = filePath + ".bak";
            File backupFile = new File(backupFilePath);
            Files.move(dest.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

//        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
//            List<Map<String, Object>> excelData = new ArrayList<>();
//            // 엑셀 파일 읽기 로직 추가
//
//            try (Workbook newWorkbook = new XSSFWorkbook()) {
//                // 엑셀 파일 작성 로직 추가 (excelData를 이용하여 시트, 행, 셀 작성)
//
//                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
//                    newWorkbook.write(bos);
//                }
//            }
//        }
        file.transferTo(new File(filePath));

        return AdminServiceExcelSaveResponse.builder()
                .fileName(filePath)
                .size(file.getSize())
                .fileType(file.getContentType())
                .build();
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
