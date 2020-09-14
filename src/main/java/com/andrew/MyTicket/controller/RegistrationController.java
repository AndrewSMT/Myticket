package com.andrew.MyTicket.controller;

import com.andrew.MyTicket.model.User;
import com.andrew.MyTicket.repositories.UserRepo;
import com.andrew.MyTicket.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RegistrationController control all registration request
 *
 * @author Andreii Matveiev
 * @author andrei.matviev@gmail.com
 */
@Controller
@RequestMapping("/registration")
public class RegistrationController {

    /**
     * Declaration UserService variable for dependency injection
     */
    private final UserService userService;

    /**
     * Declaration UserRepo variable for dependency injection
     */
    private final UserRepo userRepo;

    /**
     * Dependency injection into RegistrationController with constructor
     *
     * @param userService Inject Ticket repository
     * @param userRepo    Inject Order service
     */
    public RegistrationController(UserService userService, UserRepo userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
    }

    /**
     * Open registration page
     *
     * @return String registration page
     */
    @GetMapping
    public String registration() {
        return "registration";
    }

    /**
     * Add new user into database
     *
     * @param passwordConfirm Get password confirm
     * @param user            Get new User
     * @param bindingResult   Get list of valid error
     * @param model           Model of page
     * @return String name of registration page or String redirect Login page if incorrect data
     */
    @PostMapping
    public String addUser(@RequestParam("password2") String passwordConfirm,
                          @Valid User user, BindingResult bindingResult, Model model) {
        model.addAttribute("user", user);
        boolean Success = true;
        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            Success = false;
            model.addAttribute("usernameError", "User exist");
        }

        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$");
        Matcher matcher = pattern.matcher(user.getPassword());

        while (!matcher.find()) {
            model.addAttribute("passwordError", "Password must contain lowercase and uppercase latin letters, numbers");
            return "registration";
        }

        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Password confirmation cannot be empty");
            Success = false;
        }

        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Password are different");
            Success = false;
        }

        if (bindingResult.hasErrors() || userFromDb != null) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }

        if (Success) {
            userService.addUser(user);
        }

        return "redirect:/login";
    }


    /**
     * Get activate code from User
     *
     * @return String Login page
     */
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
