package ru.ivanov.vinitro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vinitro")
public class MainController {

    @GetMapping("/index")
    public String showIndex(){
        return "vinitro_index";
    }
}
