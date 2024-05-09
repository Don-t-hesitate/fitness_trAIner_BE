package com.example.fitness_trAIner.controller.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // 모든 에러 페이지 요청에 대해 index.html로 응답
        return "index";
    }

    public String getErrorPath() {
        return "/error";
    }
}