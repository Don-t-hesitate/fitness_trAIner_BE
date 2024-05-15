package com.example.fitness_trAIner.service.ai.dto.request;

import com.example.fitness_trAIner.vos.AI.xAndyVO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AIServiceRequest {
    private xAndyVO leftAnkle;
    private xAndyVO leftElbow;
    private xAndyVO leftFootIndex;
    private xAndyVO leftHeel;
    private xAndyVO leftHip;
    private xAndyVO leftIndex;
    private xAndyVO leftKnee;
    private xAndyVO leftPinky;
    private xAndyVO leftShoulder;
    private xAndyVO leftThumb;
    private xAndyVO leftWrist;
    private xAndyVO rightAnkle;
    private xAndyVO rightElbow;
    private xAndyVO rightFootIndex;
    private xAndyVO rightHeel;
    private xAndyVO rightHip;
    private xAndyVO rightIndex;
    private xAndyVO rightKnee;
    private xAndyVO rightPinky;
    private xAndyVO rightShoulder;
    private xAndyVO rightThumb;
    private xAndyVO rightWrist;
    private String workoutName;
}
