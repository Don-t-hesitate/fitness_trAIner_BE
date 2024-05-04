package com.example.fitness_trAIner.common.scheduler;

import com.example.fitness_trAIner.repository.user.UserScoreRepository;
import com.example.fitness_trAIner.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResetScoreScheduler {
    private final UserScoreRepository userScoreRepository;

    @Scheduled(cron = "0 0 0 */2 * MON")
    public void resetScoreDB() {
        userScoreRepository.deleteAll();
    }
}
