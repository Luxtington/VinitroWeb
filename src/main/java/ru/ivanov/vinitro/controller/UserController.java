package ru.ivanov.vinitro.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.vinitro.model.Role;
import ru.ivanov.vinitro.model.User;
import ru.ivanov.vinitro.service.UserService;
import ru.ivanov.vinitro.util.UserValidator;

import java.time.Year;
import java.util.Date;

@Controller
@RequestMapping("/vinitro/users")
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping()
    public String showAllUsers(Model model){
        model.addAttribute("all_users", userService.findAllUsers());
        model.addAttribute("users_quantity", userService.findAllUsers().size());
        return "all_users";
    }

    @GetMapping("/{id}")
    public String showConcreteUser(@PathVariable("id") String id, Model model){
        model.addAttribute("user", userService.findById(id).orElse(new User()));
        model.addAttribute("currYear", Year.now().getValue());
        return "concrete_user";
    }

    @GetMapping("/create")
    public String showCreateUserPage(@ModelAttribute("user_to_create") User user){
        return "user_create_form";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user_to_create") @Valid User user,
                             BindingResult bindingResult){

        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()){
            return "user_create_form";
        }
        userService.save(user);
        return "redirect:/vinitro/users";
    }


    @GetMapping("/edit/{id}")
    public String showEditUserPage(@PathVariable("id") String id, Model model){
        model.addAttribute("user_to_edit", userService.findById(id).orElse(null));
        return "user_update_form";
    }

    @PatchMapping("/edit/{id}")
    public String editUser(@PathVariable("id") String id, @ModelAttribute("user_to_edit")  @Valid User user){
        userService.save(id, user);
        return "redirect:/vinitro/users";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") String id){
        userService.deleteById(id);
        return "redirect:/vinitro/users";
    }

    @PostMapping("/employ/nurse/{id}")
    public String employNurse(@PathVariable("id") String id, Model model){
        User user = userService.findById(id).orElse(null);
        userService.addRoleToUser(user, new Role("ROLE_NURSE"));
        return "redirect:/vinitro/users/" + id;
    }

    @PostMapping("/employ/assistant/{id}")
    public String employAssistant(@PathVariable("id") String id, Model model){
        User user = userService.findById(id).orElse(null);
        userService.addRoleToUser(user, new Role("ROLE_ASSISTANT"));
        return "redirect:/vinitro/users/" + id;
    }
}
