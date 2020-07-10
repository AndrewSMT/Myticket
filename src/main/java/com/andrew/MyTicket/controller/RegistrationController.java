package com.andrew.MyTicket.controller;

import com.andrew.MyTicket.model.User;
import com.andrew.MyTicket.repositories.UserRepo;
import com.andrew.MyTicket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String registration() {
        return "registration";
    }

    @PostMapping
    public String addUser(@RequestParam("password2") String passwordConfirm,
                          @Valid User user,
                          BindingResult bindingResult, Model model) {
        model.addAttribute("user", user);
        boolean Success = true;
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            Success = false;
        }
        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Password confirmation cannot be empty");
            Success = false;
        }
        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Password are different");
            Success = false;
        }
        if (!Success) {
            model.addAttribute("usernameError", "User exist");
        }
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }
        if(Success){
            userService.addUser(user);
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "Email successfully activate");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found");
        }
        return "login";
    }
}
