package com.andrew.MyTicket.controller;

import com.andrew.MyTicket.repositories.EventRepo;
import com.andrew.MyTicket.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class MainController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private EventRepo eventRepo;

    @GetMapping
    public String list( Model model){
        model.addAttribute("events", eventRepo.findAll());
        return "main";
    }
    // Login form
    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
