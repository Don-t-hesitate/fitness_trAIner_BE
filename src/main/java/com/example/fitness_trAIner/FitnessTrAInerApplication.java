package com.example.fitness_trAIner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJpaAuditing
//TODO 랭킹 시스템 구현 필요
//TODO 출석체크, 2주마다 점수 초기화 구현 필요
public class FitnessTrAInerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitnessTrAInerApplication.class, args);
    }

}
