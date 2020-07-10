package com.andrew.MyTicket.controller;


import com.andrew.MyTicket.dto.AddTicketsDto;
import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.model.Ticket;
import com.andrew.MyTicket.model.User;
import com.andrew.MyTicket.repositories.*;
import com.andrew.MyTicket.service.AdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {


    @Autowired
    private PlaceRepo placeRepo;

    @Autowired
    private AdminService adminService;

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TicketRepo ticketRepo;

    @Autowired
    private OrderRepo orderRepo;



    @GetMapping("/addEvent")
    public String getAddEventPage(Model model) {
        model.addAttribute("places", placeRepo.findAll());
        return "addEvent";
    }

    @GetMapping("/addTickets")
    public String getAddTicketPage(Model model) {
        return "addTickets";
    }

    @PostMapping("/addEvent")
    public String addEvent(Event event, @RequestParam("file") MultipartFile file, Model model) throws IOException {
        boolean exist = adminService.addEvent(event, file);
        if (!exist) {
            model.addAttribute("message", "Event exist!");
        }
        return "redirect:/admin/addEvent";
    }

    @PostMapping("/addTickets")
    public String addTickets(AddTicketsDto addTicketsDto, Model model) {
        Map<String, Boolean> stringBooleanMap = adminService.addTickets(addTicketsDto);

        for (Map.Entry<String, Boolean> entry : stringBooleanMap.entrySet()) {
            if (!entry.getValue()) {
                model.addAttribute("message", entry.getKey());
            } else {
                model.addAttribute("message", "Tickets added successful");
            }
        }
/*
        if(adminService.addTickets(addTicketsDto)){
            model.addAttribute("message1", "Tickets added successful");
        }else{
            model.addAttribute("message1", "Tickets don't added");
        }*/
        return "addTickets";
    }

   /* @GetMapping("{id}")
    public String userEdit(@PathVariable("id") User user, Model model) {
        model.addAttribute(user.getUsername());
        return "adminFunc";
    }
*/
    @GetMapping("/userList")
    public String getUserListPage(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "adminFunc";
    }

    @GetMapping("/checkEvents")
    public String getEventListPage(Model model) {
        model.addAttribute("events", eventRepo.findAll());
        model.addAttribute("places", placeRepo.findAll());
        return "adminFunc";
    }

    @PostMapping("/editEvents/{id}")
    public String editEvents(@PathVariable("id") Long id,Event event,@RequestParam("file") MultipartFile file, Model model) {
        event.setId(id);
        event.setPicture("http://localhost:8080/picture/" + file.getOriginalFilename());
        eventRepo.save(event);
        model.addAttribute("events", eventRepo.findAll());
        return "adminFunc";
    }


    @GetMapping("/delEvents/{id}")
    public String delEvents(@PathVariable("id") Event event, Model model) {
        eventRepo.delete(event);
        model.addAttribute("events", eventRepo.findAll());
        return "adminFunc";
    }

    @GetMapping("/checkTickets")
    public String getTicketListPage(Model model) {
        model.addAttribute("tickets", ticketRepo.findAll());
        return "adminFunc";
    }

    @GetMapping("/delTickets/{id}")
    public String delTickets(@PathVariable("id") Ticket ticket, Model model) {
        ticketRepo.delete(ticket);
        model.addAttribute("tickets", ticketRepo.findAll());
        return "adminFunc";
    }

    @GetMapping("/checkOrders")
    public String getOrderListPage(Model model) {
        model.addAttribute("orders", orderRepo.findAll());
        return "adminFunc";
    }


  /*  @PutMapping("{id}")
    public String update(@PathVariable("id") User userFromDb, @RequestBody User user) {
        BeanUtils.copyProperties(user, userFromDb, "id");
        userRepo.save(userFromDb);
        return "adminFunc";
    }*/

}
