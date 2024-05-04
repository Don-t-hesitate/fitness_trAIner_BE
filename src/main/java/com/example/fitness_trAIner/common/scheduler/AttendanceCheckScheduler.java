package com.example.fitness_trAIner.common.scheduler;

import com.example.fitness_trAIner.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttendanceCheckScheduler {
    private final UserService userService;

    @Scheduled(cron = "0 0 0 * * *")
    public void resetAttendance() {
        userService.resetAttendance();
    }
}
