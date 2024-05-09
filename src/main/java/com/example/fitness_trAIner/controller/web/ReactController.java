package com.example.fitness_trAIner.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ReactController {

    // ceprj.gachon.ac.kr:60008/ 로 접속하면 index.html(react 정적파일)을 반환
    // ceprj.gachon.ac.kr:60008/~~~/~~~ 같이 url의 하위 path가 둘 이상인 경우도 상정
//    @GetMapping({"/**/{path:[^\\.]*}"})
    @GetMapping({"/"})
    public String getIndex() {
        return "index.html";
    }

//    @GetMapping({"/exercise/add"})
//    public String getExercise() {
//        return "index.html";
//    }
}
