package com.example.fitness_trAIner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJpaAuditing
@EnableScheduling
//TODO 식단, 출석이 반영되는 점수 테이블 이름
//TODO 2주마다 초기화되는거 시간 설정
public class FitnessTrAInerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitnessTrAInerApplication.class, args);
    }

}
