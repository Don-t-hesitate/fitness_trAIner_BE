package com.example.fitness_trAIner.service.admin.dto.response;

import com.example.fitness_trAIner.vos.UserVO;
import lombok.*;

import java.util.List;
@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminServiceFindUserListResponse {
    private List<UserVO> userList;
}
