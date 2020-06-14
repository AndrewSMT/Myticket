package com.andrew.MyTicket.controller;

import com.andrew.MyTicket.model.User;
import com.andrew.MyTicket.repositories.UserRepo;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("{id}")
    public String userEdit (@PathVariable("id") User user,Model model){
        model.addAttribute(user.getUsername());
        return "user";
    }
    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "user";
    }
    @PutMapping("{id}")
    public String update(@PathVariable("id") User userFromDb, @RequestBody User user){
        BeanUtils.copyProperties(user,userFromDb,"id");
        userRepo.save(userFromDb);
        return "user";
    }
}

