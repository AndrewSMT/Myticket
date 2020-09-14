package com.andrew.MyTicket.controller;

import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.model.Ticket;
import com.andrew.MyTicket.repositories.*;
import com.andrew.MyTicket.service.AdminService;
import com.andrew.MyTicket.transfer.AddTicketsDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * UserController
 *
 * @author Andreii Matveiev
 * @author andrei.matviev@gmail.com
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    /**
     * Declaration PlaceRepo variable for dependency injection
     */
    private final PlaceRepo placeRepo;

    /**
     * Declaration EventRepo variable for dependency injection
     */
    private final EventRepo eventRepo;

    /**
     * Declaration UserRepo variable for dependency injection
     */
    private final UserRepo userRepo;

    /**
     * Declaration TicketRepo variable for dependency injection
     */
    private final TicketRepo ticketRepo;

    /**
     * Declaration OrderRepo variable for dependency injection
     */
    private final OrderRepo orderRepo;

    /**
     * Declaration AdminService variable for dependency injection
     */
    private final AdminService adminService;

    /**
     * Dependency injection into AdminController with constructor
     *
     * @param placeRepo    Inject Place repository
     * @param eventRepo    Inject Event repository
     * @param userRepo     Inject User repository
     * @param ticketRepo   Inject Ticket repository
     * @param orderRepo    Inject Order repository
     * @param adminService Inject Administrator service
     */
    public AdminController(PlaceRepo placeRepo, EventRepo eventRepo, UserRepo userRepo, TicketRepo ticketRepo,
                           OrderRepo orderRepo, AdminService adminService) {
        this.placeRepo = placeRepo;
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
        this.ticketRepo = ticketRepo;
        this.orderRepo = orderRepo;
        this.adminService = adminService;
    }

    /**
     * Open addEvent page
     *
     * @param model Model of page
     * @return String addEvent page
     */
    @GetMapping("/addEvent")
    public String getAddEventPage(Model model) {
        model.addAttribute("places", placeRepo.findAll());
        return "addEvent";
    }

    /**
     * Open addTicket page
     *
     * @return String addTickets page
     */
    @GetMapping("/addTickets")
    public String getAddTicketPage() {
        return "addTickets";
    }

    /**
     * Open adminFunc page
     * With list of all users
     *
     * @param model Model of page
     * @return String adminFunc page
     */
    @GetMapping("/userList")
    public String getUserListPage(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "adminFunc";
    }

    /**
     * Open adminFunc page
     * With list of all events and editor of them
     *
     * @return String adminFunc page
     */
    @GetMapping("/checkEvents")
    public String getEventListPage(Model model) {
        model.addAttribute("events", eventRepo.findAll());
        model.addAttribute("places", placeRepo.findAll());
        return "adminFunc";
    }

    /**
     * Open adminFunc page
     * With list of all tickets and editor of them
     *
     * @return String adminFunc page
     */
    @GetMapping("/checkTickets")
    public String getTicketListPage(Model model) {
        model.addAttribute("tickets", ticketRepo.findAll());
        return "adminFunc";
    }

    /**
     * Open adminFunc page
     * With list of all orders
     *
     * @return String adminFunc page
     */
    @GetMapping("/checkOrders")
    public String getOrderListPage(Model model) {
        model.addAttribute("orders", orderRepo.findAll());
        return "adminFunc";
    }

    /**
     * Delete event by input id
     *
     *c
     * @return String adminFunc page
     */
    @GetMapping("/delEvents/{id}")
    public String delEvents(Model model, @PathVariable("id") Event event) {
        eventRepo.delete(event);
        model.addAttribute("events", eventRepo.findAll());
        return "adminFunc";
    }

    /**
     * Delete ticket by input id
     *
     * @param model  Model of page
     * @param ticket Get ticket by  path variable id
     * @return String adminFunc page
     */
    @GetMapping("/delTickets/{id}")
    public String delTickets(Model model, @PathVariable("id") Ticket ticket) {
        ticketRepo.delete(ticket);
        model.addAttribute("tickets", ticketRepo.findAll());
        return "adminFunc";
    }

    /**
     * Add new event into database
     *
     * @param model Model of page
     * @param event Get an instance of the Event being added
     * @param file  A representation of an uploaded file
     * @return String redirect on addEvent page
     * @throws IOException
     */
    @PostMapping("/addEvent")
    public String addEvent(Model model, Event event, @RequestParam("file") MultipartFile file) throws IOException {
        boolean exist = adminService.addEvent(event, file);
        if (!exist) {
            model.addAttribute("message", "Event exist!");
        }
        return "redirect:/admin/addEvent";
    }

    /**
     * Add new tickets into database
     *
     * @param model         Model of page
     * @param addTicketsDto Take  DTO for ticket
     * @return String addTickets page
     */
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

        return "addTickets";
    }


    /**
     * Edit selected event by id
     *
     * @param model   Model of page
     * @param eventId Id of Event
     * @param event   Get instance of the Event being edited with new param
     * @param file    A representation of an uploaded file
     * @return String adminFunc page
     */
    @PostMapping("/editEvents/{id}")
    public String editEvents(Model model, @PathVariable("id") Long eventId, Event event, @RequestParam("file") MultipartFile file) {
        event.setId(eventId);

        if (!file.getOriginalFilename().equals("")) {
            event.setPicture("http://localhost:8080/picture/" + file.getOriginalFilename());
        }
        eventRepo.save(event);
        model.addAttribute("events", eventRepo.findAll());

        return "adminFunc";
    }


}
