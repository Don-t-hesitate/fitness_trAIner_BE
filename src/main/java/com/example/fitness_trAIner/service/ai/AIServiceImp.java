package com.example.fitness_trAIner.service.ai;

import com.example.fitness_trAIner.common.exception.exceptions.AIException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Service
@Slf4j
public class AIServiceImp implements AIService{

    @Override
    public String pythonProcess(String data) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("python", "C:/ai/test.py", data);
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

        return result.toString();

    }

}
