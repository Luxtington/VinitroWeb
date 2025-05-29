package ru.ivanov.vinitro.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ivanov.vinitro.model.User;
import ru.ivanov.vinitro.repository.UserRepository;
import ru.ivanov.vinitro.service.AuthService;
import ru.ivanov.vinitro.util.UserValidator;

@Controller
@RequestMapping("/vinitro/auth")
public class AuthController {

    private final AuthService authService;
    private final UserValidator userValidator;

    @Autowired
    public AuthController(AuthService authService, UserValidator userValidator, UserRepository userRepository) {
        this.authService = authService;
        this.userValidator = userValidator;
    }

    @GetMapping("/login")
    public String showLoginPage(){
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(@ModelAttribute("user") User user){
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User user,
                               BindingResult bindingResult){

        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()){
            return "redirect:/register";
        }

        try {
            authService.register(user);
            return "redirect:/login";
        } catch (RuntimeException e) {
            bindingResult.rejectValue("username", "", e.getMessage());
            return "register";
        }
    }
}
