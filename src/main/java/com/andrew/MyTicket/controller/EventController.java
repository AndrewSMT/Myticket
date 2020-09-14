package com.andrew.MyTicket.controller;

import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.model.Ticket;
import com.andrew.MyTicket.model.TicketStatus;
import com.andrew.MyTicket.repositories.TicketRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controller of event requests
 *
 * @author Andreii Matveiev
 * @author andrei.matviev@gmail.com
 */
@Controller
@RequestMapping("/event")
public class EventController {

    /**
     * Declaration TicketRepo variable for dependency injection
     */
    private final TicketRepo ticketRepo;

    /**
     * Dependency injection into EventController with constructor
     *
     * @param ticketRepo   Inject TicketRepo
     */
    public EventController(TicketRepo ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    /**
     * Open event page
     *
     * @return String event page
     */
    @GetMapping
    public String getEventPage() {
        return "event";
    }

    /**
     * Open event page by id
     * @param event Get event by path variable id
     * @param model Model of page
     * @return String event page
     */
    @GetMapping("{id}")
    public String getOneEvent(@PathVariable("id") Event event, Model model) {
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
