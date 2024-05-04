package com.example.fitness_trAIner.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReactController {

    // ceprj.gachon.ac.kr:60008/ 로 접속하면 index.html(react 정적파일)을 반환
    @GetMapping({"/", "/{x:[\\w\\-]+}"})
    public String getIndex() {
        return "index.html";
    }
}
