package com.andrew.MyTicket.mobController;

import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.model.Ticket;
import com.andrew.MyTicket.model.TicketStatus;
import com.andrew.MyTicket.repositories.EventRepo;
import com.andrew.MyTicket.repositories.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mobile/")
public class MainJsonController {

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private TicketRepo ticketRepo;
    @GetMapping
    public List<Event> list(){
        List<Event> list = eventRepo.findAll();
        return list;
    }
    @GetMapping("/event/inf/{id}")
    public List<Integer> listInfo(@PathVariable("id") Event event){
        List<Integer> list = new ArrayList();
        Integer min = ticketRepo.findTicketByMinPrice(event);
        Integer max = ticketRepo.findTicketByMaxPrice(event);
        List<Ticket> tickets = ticketRepo.findTicketsByEventAndTicketStatus(event, TicketStatus.ACTIVE);
        list.add(min);
        list.add(max);
        list.add(tickets.size());
        return list;
    }
}
