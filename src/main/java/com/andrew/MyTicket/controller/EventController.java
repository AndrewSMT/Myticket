package com.andrew.MyTicket.controller;

import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.model.Ticket;
import com.andrew.MyTicket.model.TicketStatus;
import com.andrew.MyTicket.repositories.EventRepo;
import com.andrew.MyTicket.repositories.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private TicketRepo ticketRepo;


    @GetMapping
    public String get( Model model) {
        return "event";
    }

    @GetMapping("{id}")
    public String getEvent(@PathVariable("id") Event event, Model model) {
        Integer min = ticketRepo.findTicketByMinPrice(event);
        Integer max = ticketRepo.findTicketByMaxPrice(event);
        List<Ticket> tickets = ticketRepo.findTicketsByEventAndTicketStatus(event, TicketStatus.ACTIVE);
        model.addAttribute("event", event);
        model.addAttribute("min", min);
        model.addAttribute("max", max);
        model.addAttribute("count", tickets.size());
        return "event";
    }
}
