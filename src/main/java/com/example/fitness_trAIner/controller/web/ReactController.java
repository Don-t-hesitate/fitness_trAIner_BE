package com.example.fitness_trAIner.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReactController {
    @GetMapping({"/", "/{x:[\\w\\-]+}"})
    public String getIndex() {
        return "index.html";
    }
}
