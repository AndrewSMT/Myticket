package com.andrew.MyTicket.controller;


import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.repositories.EventRepo;
import com.andrew.MyTicket.repositories.PlaceRepo;
import com.andrew.MyTicket.service.AdminService;
import com.andrew.MyTicket.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class adminController {

    @Autowired
    private PlaceRepo placeRepo;

    @Autowired
    private AdminService adminService;

    @Autowired
    private EventRepo eventRepo;

    @GetMapping("/addEvent")
    public String getAddEventPage(Model model){
        model.addAttribute("places",placeRepo.findAll());

        return "addEvent";
    }

    @GetMapping("/addTickets")
    public String getAddTicketsPage(){
        return "addTickets";
    }

    @PostMapping("/addEvent")
    public String addEvent(Event event, @RequestParam("file") MultipartFile file, Model model) throws IOException {
        boolean exist = adminService.addEvent(event,file);
        if(!exist) {
            model.addAttribute("message", "Event exist!");
        }
        return "redirect:/admin/addEvent";
    }
}
