package com.example.fitness_trAIner.service.admin;

import com.example.fitness_trAIner.common.exception.exceptions.*;
import com.example.fitness_trAIner.repository.diet.Food;
import com.example.fitness_trAIner.repository.diet.FoodRepository;
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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class AdminServiceImp implements AdminService{

    @Autowired
    private TransactionTemplate transactionTemplate;

    private final UserRepository userRepository;
    private final WorkoutVideoRepository workoutVideoRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final FoodRepository foodRepository;

    @Value("${foodpath}")
    private String excelPath;
    @Value("${apkpath}")
    private String appPath;

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
        user.setPreferenceFoods(request.getPreferenceFoods());

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
    public byte[] getExcelFileBytes(String filePath) throws IOException {
        String fileDestPath = excelPath + File.separator + filePath;

        File file = new File(fileDestPath);
        InputStream inputStream = new FileInputStream(file);
        byte[] excelFile = IOUtils.toByteArray(inputStream);
        inputStream.close();

        return excelFile;
    }

    @Override
    public AdminServiceExcelSaveResponse saveExcelData(MultipartFile file, String filePath) throws IOException {
        String fileDestPath = excelPath + File.separator + filePath;
        File dest = new File(fileDestPath);

        System.out.println("file : " + dest.getAbsolutePath());
        System.out.println("backup : " + dest.getParent() + File.separator + "food_db_result_before_update.xlsx");
        // 기존 파일이 있으면 파일 이름을 "food_db_result_before_update"로 변경하여 백업
        if (dest.exists()) {
            String backupFilePath = dest.getParent() + File.separator + "food_db_result_before_update.xlsx";
            File backupFile = new File(backupFilePath);
            Files.copy(dest.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            if (dest.delete()) {
                System.out.println("기존 파일 삭제 성공");
            } else {
                throw new IOException("기존 파일 삭제 실패", new FileStoreException("기존 파일 삭제 실패"));
            }
        }

        file.transferTo(new File(fileDestPath));

        // 엑셀 파일을 읽어서 food db에 변경 사항 반영
        transactionTemplate.executeWithoutResult(status -> {
            try {
                System.out.println("파일 읽기 전임");
                // 엑셀 파일 읽기
                Workbook workbook1 = new XSSFWorkbook(new FileInputStream(excelPath + File.separator + "food_db_result_before_update.xlsx"));
                Workbook workbook2 = new XSSFWorkbook(new FileInputStream(excelPath + File.separator + "food_db_result_final.xlsx"));
                System.out.println("일단 파일 두 개 불러옴");

                // 각 행을 POJO로 변환 후 리스트에 저장
                List<Food> list1 = readWorkbook(workbook1);
                System.out.println("list1 읽음");
                List<Food> list2 = readWorkbook(workbook2);

                // 음식 아이디 순으로 정렬
                Collections.sort(list1);
                Collections.sort(list2);

                // 삭제된 행 찾기
                List<Food> deletedRows = new ArrayList<>(list1);
                deletedRows.removeAll(list2);

                // 수정된 행 찾기
                List<Food> changedRows = new ArrayList<>();
                int i = 0, j = 0;
                System.out.println("list1 size: " + list1.size() + ", list2 size: " + list2.size());
                while (i < list1.size() && j < list2.size()) {
                    Food food1 = list1.get(i);
                    Food food2 = list2.get(j);
                    int compare = food1.getFoodId().compareTo(food2.getFoodId());
                    if (compare == 0) {
                        if (!food1.isChanged(food2)) { // 필드 중 하나라도 다르면 수정된 것으로 처리
                            changedRows.add(food2);
                        }
                        i++;
                        j++;
                    } else if (food1.compareTo(food2) < 0) { // list1의 foodId가 더 작음
                        i++;
                    } else { // list2의 foodId가 더 작음
                        j++;
                    }
                }

                // 추가된 행 찾기
                List<Food> addedRows = new ArrayList<>(list2);
                addedRows.removeAll(list1);

                for (Food food : deletedRows) {
                    System.out.println("삭제할 음식: ");
                    System.out.println(food.getFoodName());
                    foodRepository.delete(food);
                }

                System.out.println("수정된 행: " + changedRows);
                for (Food food : changedRows) {
                    System.out.println("수정할 음식: ");
                    System.out.println(food.getFoodName());
                    foodRepository.save(food);
                }

                System.out.println("추가된 행: " + addedRows);
                for (Food food : addedRows) {
                    System.out.println("추가할 음식: ");
                    System.out.println(food.getFoodName());
                    foodRepository.save(food);
                }

                System.out.println("list1: ");
                for (Food food : list1)
                    System.out.println(food.getFoodId());
                System.out.println("list2: ");
                for (Food food : list2)
                    System.out.println(food.getFoodId());

                workbook1.close();
                workbook2.close();
            } catch (IOException e) {
                // 예외 처리
                throw new FileStoreException("엑셀 파일 읽기 중 오류 발생");
            }
        });


        return AdminServiceExcelSaveResponse.builder()
                .fileName(fileDestPath)
                .size(file.getSize())
                .fileType(file.getContentType())
                .build();
    }

    private List<Food> readWorkbook(Workbook workbook) {
        List<Food> data = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        int count = 0;
        for (Row row : sheet) {
            count++;
            // 첫 번째 행은 헤더이므로 skip
            if (row.getRowNum() == 0) {
                continue;
            }
            Row dataRow = sheet.getRow(row.getRowNum());
            System.out.println("현재 row 수: " + count);
            Food food = mapRowToFood(dataRow);
            data.add(food);
        }
        return data;
    }

    private Food mapRowToFood(Row row) {
        double calories = 0;
        double protein = 0;
        double fat = 0;
        double carbohydr = 0;

        for (Cell cell : row) {
            if (cell.getCellType() == CellType.STRING) {
                System.out.println("현재 cell: " + cell.getStringCellValue());
            } else if (cell.getCellType() == CellType.NUMERIC) {
                System.out.println("현재 cell: " + cell.getNumericCellValue());
            } else {
                System.out.println("현재 cell: " + cell.getCellType());
            }
        }

        if (row.getCell(4).getCellType() == CellType.STRING) {
            calories = Double.parseDouble(row.getCell(4).getStringCellValue());
        } else if (row.getCell(4).getCellType() == CellType.NUMERIC) {
            calories = row.getCell(4).getNumericCellValue();
        }

        if (row.getCell(5).getCellType() == CellType.STRING) {
            protein = Double.parseDouble(row.getCell(5).getStringCellValue());
        } else if (row.getCell(5).getCellType() == CellType.NUMERIC) {
            protein = row.getCell(5).getNumericCellValue();
        }

        if (row.getCell(6).getCellType() == CellType.STRING) {
            fat = Double.parseDouble(row.getCell(6).getStringCellValue());
        } else if (row.getCell(6).getCellType() == CellType.NUMERIC) {
            fat = row.getCell(6).getNumericCellValue();
        }

        if (row.getCell(7).getCellType() == CellType.STRING) {
            carbohydr = Double.parseDouble(row.getCell(7).getStringCellValue());
        } else if (row.getCell(7).getCellType() == CellType.NUMERIC) {
            carbohydr = row.getCell(7).getNumericCellValue();
        }

        System.out.println("끝");
        return Food.builder()
                .foodId(row.getCell(0).getStringCellValue())
                .foodName(row.getCell(1).getStringCellValue())
                .mainType(row.getCell(2).getStringCellValue())
                .detailType(row.getCell(3).getStringCellValue())
                .calories(calories)
                .protein(protein)
                .fat(fat)
                .carbohydr(carbohydr)
                .taste(row.getCell(8).getStringCellValue())
                .mainIngredient(row.getCell(9).getStringCellValue())
                .secondaryIngredient(row.getCell(10).getStringCellValue())
                .cookingMethod(row.getCell(11).getStringCellValue())
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

    @Override
    public byte[] getApp() throws IOException {
        File file = new File(appPath);
        InputStream inputStream = null;

        // file 디렉터리 아래의 파일 중 .apk 확장자를 가진 파일을 찾아서 inputStream에 저장
        for (File f : file.listFiles()) {
            if (f.getName().endsWith(".apk")) {
                try {
                    inputStream = new FileInputStream(f);
                } catch (FileNotFoundException e) {
                    throw new FileStoreException("앱 파일 읽기 실패");
                }
            }
        }
        if (inputStream == null) {
            throw new FileStoreException("앱 파일 없음");
        }

        // inputStream을 byte[]로 변환
        byte[] appFile = null;
        appFile = IOUtils.toByteArray(inputStream);
        inputStream.close();

        return appFile;
    }

}
