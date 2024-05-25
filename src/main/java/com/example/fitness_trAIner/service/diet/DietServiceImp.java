package com.example.fitness_trAIner.service.diet;

import com.example.fitness_trAIner.common.exception.exceptions.DietException;
import com.example.fitness_trAIner.common.exception.exceptions.NoUserException;
import com.example.fitness_trAIner.repository.diet.Diet;
import com.example.fitness_trAIner.repository.diet.DietRepository;
import com.example.fitness_trAIner.repository.diet.Food;
import com.example.fitness_trAIner.repository.diet.FoodRepository;
import com.example.fitness_trAIner.repository.user.User;
import com.example.fitness_trAIner.repository.user.UserRepository;
import com.example.fitness_trAIner.repository.user.UserScore;
import com.example.fitness_trAIner.repository.user.UserScoreRepository;
import com.example.fitness_trAIner.service.diet.dto.DietServiceInitialFoodList;
import com.example.fitness_trAIner.service.diet.dto.DietServiceUserFoodInfo;
import com.example.fitness_trAIner.service.diet.dto.request.DietServiceRecommendRequest;
import com.example.fitness_trAIner.service.diet.dto.request.DietServiceSaveDayOfUsersRequest;
import com.example.fitness_trAIner.service.diet.dto.response.DietServiceRecommendResponse;
import com.example.fitness_trAIner.vos.DietVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DietServiceImp implements DietService{

    private final UserRepository userRepository;
    private final DietRepository dietRepository;
    private final FoodRepository foodRepository;
    private final UserScoreRepository userScoreRepository;
    @Value("${foodpath}")
    private String foodPath;

    private final EntityManager entityManager;


    @Override
    public DietServiceRecommendResponse recommendDiet(DietServiceRecommendRequest request) throws IOException {

        Long userId = Optional.ofNullable(request.getUserId()).orElseThrow(() -> new NoUserException("유저 아이디가 필요합니다."));

        // user 테이블에서 userId에 해당하는 사용자의 선호 음식 가져옴
        String preferenceTypeFood = userRepository.findById(userId).get().getPreferenceFoods();
        String[] preferArray = preferenceTypeFood.split(",");
        List<DietServiceInitialFoodList> foodList = getFoodList(foodPath + File.separator + "select_food_list.csv");

        List<DietServiceInitialFoodList> matchingFoodList = new ArrayList<>();
        for (String prefer : preferArray) {
            matchingFoodList.addAll(foodList.stream()
                    .filter(food -> food.getFoodName().equals(prefer))
                    .toList());

        }

        // user 테이블에서 userId에 해당하는 사용자의 키, 몸무게, 나이, 활동지수(activity_level) 및 매운맛 선호도, 육류 섭취여부, 선호하는 맛, 선호하는 음식 타입, 선호 음식 정보를 가져옴
        DietServiceUserFoodInfo userFoodInfo = entityManager.createQuery(
                        "SELECT new com.example.fitness_trAIner.service.diet.dto.DietServiceUserFoodInfo(" +
                                "u.userId, u.height, u.weight, u.age, u.gender, u.spicyPreference, " +
                                "u.meatConsumption, u.tastePreference, u.activityLevel, u.preferenceTypeFood, u.preferenceFoods) " +
                                "FROM User u " +
                                "WHERE u.userId = :userId", DietServiceUserFoodInfo.class)
                .setParameter("userId", userId)
                .getSingleResult();

        // diet 테이블에서 userId에 해당하는 사용자의 식단 정보 가져오기, 사용자가 먹은 음식 정보를 가져옴
        List<Food> eatenFoodList = new ArrayList<>();
        List<Diet> dietList = dietRepository.findByUserId(userId);
        if (!dietList.isEmpty()) {
            for (Diet diet : dietList) {
                System.out.println("diet: " + diet.getFoodId() + ", " + diet.getEatDate() + ", " + diet.getTotalCalories());
                Food food = foodRepository.findById(diet.getFoodId()).get();
                eatenFoodList.add(food);
            }
        }
        for (Food food : eatenFoodList) {
            System.out.println("eatenFoodList: " + food.getFoodName());
        }


        // 사용자의 정보를 파이썬 스크립트에 파라미터로 넘겨주기
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> userData = objectMapper.convertValue(userFoodInfo, Map.class);
        userData.put("matchingFoodList", matchingFoodList);
        userData.put("eatenFoodList", eatenFoodList);
        String userDataJson = objectMapper.writeValueAsString(userData);
//        String parameter = userDataJson.replace("\"", "\""); // 파이썬 스크립트에서 사용자 정보를 받을 때 따옴표를 인식하기 위해 따옴표를 이스케이프 처리
//        for (DietServiceInitialFoodList matchnFood : matchingFoodList) {
//            System.out.println("matchnFood: " + matchnFood.getFoodName() + ", " + matchnFood.getTaste() + ", " + matchnFood.getMainIngredient() + ", " + matchnFood.getSecondaryIngredient() + ", " + matchnFood.getCookMethod());
//        }
        System.out.println("userDataJson!: " + userDataJson + "\n");

        // 파이썬 스크립트에서 사용자의 정보를 바탕으로 사용자에게 맞는 식단 추천 받기, 리눅스는 인자를 공백으로 구분하여 인식
        ProcessBuilder processBuilder = new ProcessBuilder("python", foodPath + File.separator + "food_recommend.py", userDataJson);
//        ProcessBuilder processBuilder = new ProcessBuilder("python", foodPath + File.separator + "food_recommend.py", "\""+ userDataJson.replace("\"", "\\\"") + "\""); //윈도우 용
        Process process = processBuilder.start();

        // 파이썬 스크립트에서 추천된 식단을 받아오기
        List<String> foodResultList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;

            // 파이썬 스크립트에서 출력한 결과를 한 줄씩 읽어서 리스트에 저장, 한글이 깨지지 않도록 디코딩
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();

            while ((line = reader.readLine()) != null) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(line.getBytes());
                CharBuffer charBuffer = decoder.decode(byteBuffer);
                String decodedLine = charBuffer.toString();
                System.out.println("decodedLine: " + decodedLine);
                foodResultList.add(decodedLine.replace("\\", ""));
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                List<String> errorList = new ArrayList<>();
                errorList = errorReader.lines().toList();
                log.error("파이썬 스크립트 실행 중 오류 발생: ");
                // 파이썬의 오류 메세지 다중 출력
                for (String error : errorList) {
                    log.error(error);
                }
                log.error("error: " + errorReader.readLine());
                reader.close(); errorReader.close();
                throw new IOException("파이썬 스크립트 실행 중 오류 발생" + exitCode + " " + errorReader.readLine());
            }
        } catch (IOException | InterruptedException e) {
            log.error("파이썬 스크립트 출력 읽는 중 오류 발생", e);
            throw new IOException(e);
        }

        for (String food : foodResultList) {
            System.out.println("food: " + food);
        }

        List<Map<String, Object>> foodResultMapList = objectMapper.readValue(String.join("", foodResultList), new TypeReference<List<Map<String, Object>>>() {});

        return DietServiceRecommendResponse.builder()
                .foodRecommend(foodResultMapList)
                .build();

    }

    private List<DietServiceInitialFoodList> getFoodList(String filePath) {
        List<DietServiceInitialFoodList> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // 첫 줄은 헤더이므로 읽지 않음

            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                DietServiceInitialFoodList food = new DietServiceInitialFoodList();
                food.setFoodName(fields[0]);
                food.setTaste(fields[1]);
                food.setMainIngredient(fields[2]);
                food.setSecondaryIngredient(fields[3]);
                food.setCookMethod(fields[4]);
                rows.add(food);
            }
        } catch (IOException e) {
            log.error("파일 읽는 중 오류 발생 {}", filePath ,e);
        }

        return rows;
    }

    @Override
    public Map<String, List> findDietOfDay(Long userId, String dietDate) {
        // userId가 실재하는지 확인
        if (!userRepository.existsById(userId)) {
            throw new NoUserException("존재하지 않는 사용자");
        }

        // dietDate가 '2024.05.20'의 형식이므로 java.time.LocalDate에 맞게 변환
        dietDate = dietDate.replace(".", "-");
        LocalDate localDietDate = LocalDate.parse(dietDate);

        //diet 테이블에서 userId와 dietDate에 해당하는 식단 정보를 가져옴
        List<Map> dietList = entityManager.createQuery(
                "SELECT new map(d.foodId as foodId, d.eatDate as eatDate, d.totalCalories as totalCalories) " +
                        "FROM Diet d " +
                        "WHERE d.userId = :userId AND d.eatDate = :dietDate", Map.class)
                .setParameter("userId", userId)
                .setParameter("dietDate", localDietDate)
                .getResultList();

        // 해당 음식 정보를 food 테이블에서 가져오기
        List<Map> foodList = entityManager.createQuery(
                "SELECT new map(f.foodId as foodId, f.foodName as foodName, f.calories as calories) " +
                        "FROM Food f " +
                        "WHERE f.foodId IN :foodIdList", Map.class)
                .setParameter("foodIdList", dietList.stream().map(diet -> diet.get("foodId")).toList())
                .getResultList();

        if (dietList.isEmpty()) {
            throw new DietException("해당 날짜의 식단 정보가 없습니다.");
        } else {
            // dietList와 foodList에서 음식 이름과 총 칼로리만 추출하여 반환
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (Map diet : dietList) {
                for (Map food : foodList) {
                    if (diet.get("foodId").equals(food.get("foodId"))) {
                        System.out.println("diet: " + diet.get("foodId") + ", " + food.get("foodName") + diet.get("eatDate") + ", " + diet.get("totalCalories"));
                        resultList.add(Map.of("foodName", food.get("foodName"), "totalCalories", diet.get("totalCalories")));

                    }
                }
            }
//            return Map.of("dietList", dietList, "foodList", foodList);
            return Map.of("dietList", resultList);
        }
    }

    @Override
    public String saveDiet(DietServiceSaveDayOfUsersRequest requestBody) {

        for (DietVO dietVO : requestBody.getDietList()) {
            Long userId = dietVO.getUserId();
            String date = dietVO.getDietDate();
            String eatDate = dietVO.getDietDate();
            Diet diet = new Diet();

            // userId가 실재하는지 확인
            if (!userRepository.existsById(dietVO.getUserId())) {
                throw new NoUserException("존재하지 않는 사용자");
            } else {
                // 사용자 ID 저장
                diet.setUserId(dietVO.getUserId());
            }

            // eatDate가 '2024.05.20'의 형식이므로 java.time.LocalDate에 맞게 변환하여 저장
            eatDate = eatDate.replace(".", "-");
            LocalDate localEatDate = LocalDate.parse(eatDate);
            diet.setEatDate(localEatDate);

            // foodName이 존재하는지 확인

            List<Food> foods = foodRepository.findByFoodName(dietVO.getFoodName());
            if (foods.isEmpty()) {
                throw new DietException("존재하지 않는 음식");
            } else {
                if (foods.size() > 1) {
                    for (Food food : foods) {
                        System.out.println("food: " + food.getFoodName() + ", " + food.getCalories() + ", " + food.getFoodId());
                    }
                }
                Food food = foods.get(0);
                // 음식 ID 저장
                diet.setFoodId(food.getFoodId());
                // 총 칼로리 저장
                diet.setTotalCalories(food.getCalories());
            }

            dietRepository.save(diet);

            saveDietScore(userId, date);
        }

        return "식단 저장 성공";
    }

    private int calculateDietScore(User user, double totalCalories) {
        double bmr = calculateBMR(user);
        double tdee = calculateTDEE(user, bmr);

        // 점수 범위 계산
        double lowerLimit = tdee * 0.9;
        double upperLimit = tdee * 1.1;

        // 총 칼로리가 TDEE의 90% ~ 110% 범위에 있으면 100점, 그 외에는 비례하여 점수 계산
        if (totalCalories >= lowerLimit && totalCalories <= upperLimit) {
            return 100;
        } else if (totalCalories < lowerLimit) {
            return (int) ((totalCalories / lowerLimit) * 100);
        } else {
            return (int) ((upperLimit / totalCalories) * 100);
        }
    }

    private double calculateBMR(User user) {
        // BMR 계산
        double bmr;
        if (user.getGender().equals("M")) {
            bmr = 88.362 + (13.397 * user.getWeight()) + (4.799 * user.getHeight()) - (5.677 * user.getAge());
        } else {
            bmr = 447.593 + (9.247 * user.getWeight()) + (3.098 * user.getHeight()) - (4.330 * user.getAge());
        }
        return bmr;
    }

    private double calculateTDEE(User user, double bmr) {
        // TDEE 계산
        int activityLevel = user.getActivityLevel();
        double tdee = switch (activityLevel) {
            case 1 -> bmr * 1.2;
            case 2 -> bmr * 1.375;
            case 3 -> bmr * 1.55;
            case 4 -> bmr * 1.725;
            case 5 -> bmr * 1.9;
            default -> throw new IllegalArgumentException("올바르지 않은 활동 수준입니다.");
        };

        return tdee;
    }

    private void saveDietScore(Long userId, String date) {
        LocalDate workoutDate = LocalDate.parse(date);
        Optional<UserScore> scores = userScoreRepository.findByUserIdAndExerciseName(userId, "총점");

        // 해당 날짜의 총 섭취 칼로리 계산
        double totalCalories = dietRepository.findByUserIdAndEatDate(userId, workoutDate)
                .stream()
                .mapToDouble(Diet::getTotalCalories)
                .sum();

        // 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoUserException("존재하지 않는 사용자"));

        // 식단 점수 계산
        int score = calculateDietScore(user, totalCalories);

        if (!scores.isEmpty()) {
            UserScore userScore = scores.get();
            userScore.setScore(score);
            userScoreRepository.save(userScore);
        } else {
            // UserScore에 exerciseName이 '총점'이고 userId가 같은 데이터가 있을 경우 업데이트, 없을 경우 새로 생성
            if (userScoreRepository.existsByUserIdAndExerciseName(userId, "총점")) {
                UserScore userScore = userScoreRepository.findByUserIdAndExerciseName(userId, "총점")
                        .orElseThrow(() -> new NoUserException("존재하지 않는 사용자"));
                userScore.setScore(score);
                userScoreRepository.save(userScore);
            } else {
                UserScore userScore = new UserScore();
                userScore.setUserId(userId);
                userScore.setExerciseName("총점");
                userScore.setScore(score);
                userScoreRepository.save(userScore);
            }
        }
    }
}
