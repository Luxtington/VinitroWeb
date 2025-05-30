package ru.ivanov.vinitro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ivanov.vinitro.dto.VinitroUserDetails;
import ru.ivanov.vinitro.model.User;
import ru.ivanov.vinitro.repository.UserRepository;
import ru.ivanov.vinitro.service.UserService;

@Controller
@RequestMapping("/vinitro")
public class MainController {

    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/index")
    public String showIndex(Model model, Authentication authentication){
        User user = userService.findById(((VinitroUserDetails)authentication.getPrincipal()).getId()).orElse(null);
        model.addAttribute("user", user);
        return "vinitro_index";
    }
}
