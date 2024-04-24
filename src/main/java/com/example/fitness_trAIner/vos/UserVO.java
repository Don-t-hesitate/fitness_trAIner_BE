package com.example.fitness_trAIner.vos;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class UserVO {
    private Long userId;
    private String username;
    private String nickname;
    private Integer age;
    private Float weight;
    private Float height;
}
