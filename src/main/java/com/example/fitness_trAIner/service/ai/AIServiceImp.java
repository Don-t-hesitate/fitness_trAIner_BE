package com.example.fitness_trAIner.service.ai;

import com.example.fitness_trAIner.common.exception.exceptions.AIException;
import com.example.fitness_trAIner.service.ai.dto.response.AIServiceResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Service
@Slf4j
public class AIServiceImp implements AIService{

    @Override
    public AIServiceResponse pythonProcess(String data) throws IOException {
        System.out.println(data);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("python", "C:/ai/test.py", data.replace("\"", "\\\""));
        Process process  = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder result = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result);

        try {
            int exitCode = process.waitFor();
            System.out.println("Python script exit code: " + exitCode);
            if (exitCode != 0) {
                throw new AIException("파이썬 파일 실행 결과 오류");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        String jsonResult = result.toString().replace("'", "\"");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResult);

        int perfect = jsonNode.get("perfect").asInt();
        int good = jsonNode.get("good").asInt();
        int bad = jsonNode.get("bad").asInt();

        List<String> feedbackList = new ArrayList<>();
        JsonNode feedbackNode = jsonNode.get("feedback");
        if (feedbackNode != null && feedbackNode.isArray()) {
            for (JsonNode node : feedbackNode) {
                feedbackList.add(node.asText());
            }
        }

        return AIServiceResponse.builder()
                .perfect(perfect)
                .good(good)
                .bad(bad)
                .feedback(feedbackList)
                .build();

    }

}
