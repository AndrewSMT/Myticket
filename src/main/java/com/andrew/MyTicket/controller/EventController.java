package com.andrew.MyTicket.controller;

import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.repositories.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventRepo eventRepo;

    @GetMapping
    public String get( Model model) {
        return "event";
    }

    @GetMapping("{id}")
    public String getEvent(@PathVariable("id") Event event, Model model) {
        model.addAttribute("event", event);
        return "event";
    }
}
