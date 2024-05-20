package com.example.fitness_trAIner.service.diet;

import com.example.fitness_trAIner.common.exception.exceptions.InvalidCategoryException;
import com.example.fitness_trAIner.common.exception.exceptions.NoUserException;
import com.example.fitness_trAIner.repository.user.UserRepository;
import com.example.fitness_trAIner.service.diet.dto.DietServiceInitialFoodList;
import com.example.fitness_trAIner.service.diet.dto.DietServiceUserFoodInfo;
import com.example.fitness_trAIner.service.diet.dto.request.DietServiceRecommendRequest;
import com.example.fitness_trAIner.service.diet.dto.response.DietServiceRecommendResponse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DietServiceImp implements DietService{

    private final UserRepository userRepository;
    @Value("${foodpath}")
    private String foodPath;

    private final EntityManager entityManager;


    @Override
    public DietServiceRecommendResponse recommendDiet(DietServiceRecommendRequest request) throws IOException {

        String category = Optional.ofNullable(request.getCategory()).orElseThrow(() -> new InvalidCategoryException("카테고리가 필요합니다."));
        Long userId = Optional.ofNullable(request.getUserId()).orElseThrow(() -> new NoUserException("유저 아이디가 필요합니다."));

        String preferenceTypeFood = userRepository.findById(userId).get().getPreferenceFoods();
        String[] preferArray = preferenceTypeFood.split(",");
        List<DietServiceInitialFoodList> foodList = getFoodList(foodPath + File.separator + "select_food_list.csv");

        List<DietServiceInitialFoodList> matchingFoodList = new ArrayList<>();
        for (String prefer : preferArray) {
            matchingFoodList.addAll(foodList.stream()
                    .filter(food -> food.getFoodName().equals(prefer))
                    .toList());

        }

//        return  DietServiceRecommendResponse.builder()
//                .foodRecommend(matchingFoodList)
//                .build();


//        List<String> foodList = new ArrayList<>();
//        int totalCalorie = 1480; // 예시 값
//
//        foodList = switch (category) {
//            case "korean" -> Arrays.asList("흑미밥", "된장찌개", "제육볶음", "순살 후라이드 치킨", "김치");
//            case "chinese" -> Arrays.asList("짜장면", "탕수육", "마라탕", "유산슬", "제로 탕후루");
//            case "japanese" -> Arrays.asList("스시", "우메보시", "타코야끼", "단무지", "사케");
//            case "western" -> Arrays.asList("라따뚜이", "블랙 푸딩", "알리오 올리오", "포케", "피시 앤 칩스");
//            case "fastfood" -> Arrays.asList("샌드위치", "햄버거", "라면", "떡볶이", "피자");
//            case "dessert" -> Arrays.asList("버블티", "치즈케이크", "마카롱", "티라미수", "와플");
//            default -> throw new InvalidCategoryException("Unexpected value: " + category);
//        };

//
        // user 테이블에서 userId에 해당하는 사용자의 키, 몸무게, 나이, 활동지수(activity_level) 및 매운맛 선호도, 육류 섭취여부, 선호하는 맛, 선호하는 음식 타입, 선호 음식 정보를 가져옴
        DietServiceUserFoodInfo userFoodInfo = entityManager.createQuery(
                        "SELECT new com.example.fitness_trAIner.service.diet.dto.DietServiceUserFoodInfo(" +
                                "u.userId, u.height, u.weight, u.age, u.gender, u.spicyPreference, " +
                                "u.meatConsumption, u.tastePreference, u.activityLevel, u.preferenceTypeFood, u.preferenceFoods) " +
                                "FROM User u " +
                                "WHERE u.userId = :userId", DietServiceUserFoodInfo.class)
                .setParameter("userId", userId)
                .getSingleResult();


        // 사용자의 정보를 파이썬 스크립트에 파라미터로 넘겨주기
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> userData = objectMapper.convertValue(userFoodInfo, Map.class);
        userData.put("matchingFoodList", matchingFoodList);
        String userDataJson = objectMapper.writeValueAsString(userData);
        System.out.println("userDataJson: " + userDataJson);

        // 파이썬 스크립트에서 사용자의 정보를 바탕으로 사용자에게 맞는 식단 추천 받기
        ProcessBuilder processBuilder = new ProcessBuilder("python", foodPath + File.separator + "food_recommend.py", "\""+ userDataJson.replace("\"", "\\\"") + "\"");
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
                log.error("파이썬 스크립트 실행 중 오류 발생");
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

        // foodResultList의 것들
//        List<Map<String, Object>> foodResultMapList = objectMapper.readValue(foodResultList.toString(), new TypeReference<List<Map<String, Object>>>() {});
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


}
