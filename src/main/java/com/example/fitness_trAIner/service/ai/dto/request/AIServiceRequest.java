package com.example.fitness_trAIner.service.ai.dto.request;

import com.example.fitness_trAIner.vos.PosVO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AIServiceRequest {
    private PosVO leftAnkle;
    private PosVO leftElbow;
    private PosVO leftFootIndex;
    private PosVO leftHeel;
    private PosVO leftHip;
    private PosVO leftIndex;
    private PosVO leftKnee;
    private PosVO leftPinky;
    private PosVO leftShoulder;
    private PosVO leftThumb;
    private PosVO leftWrist;
    private PosVO rightAnkle;
    private PosVO rightElbow;
    private PosVO rightFootIndex;
    private PosVO rightHeel;
    private PosVO rightHip;
    private PosVO rightIndex;
    private PosVO rightKnee;
    private PosVO rightPinky;
    private PosVO rightShoulder;
    private PosVO rightThumb;
    private PosVO rightWrist;
    private String workoutName;
}
