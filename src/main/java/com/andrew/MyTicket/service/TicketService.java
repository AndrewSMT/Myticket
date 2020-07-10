package com.andrew.MyTicket.service;

import com.andrew.MyTicket.model.Cart;
import com.andrew.MyTicket.model.Orderr;
import com.andrew.MyTicket.model.Ticket;
import com.andrew.MyTicket.model.TicketStatus;
import com.andrew.MyTicket.repositories.OrderRepo;
import com.andrew.MyTicket.repositories.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TicketService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private TicketRepo ticketRepo;

    public Cart cartDelete(Ticket ticket, Cart userCart) {


        Set<Ticket> tickets = userCart.getTicket();
        tickets.remove(ticket);
        userCart.setTicket(tickets);

        changeTicketStatus(ticket);
        return userCart;
    }

    public Ticket changeTicketStatus(Ticket ticket){
        Ticket ticketFromDb = ticketRepo.findById(ticket.getId_ticket()).orElse(new Ticket());
        ticketFromDb.getTicketStatus().clear();
        ticketFromDb.getTicketStatus().add(TicketStatus.ACTIVE);
        ticketRepo.save(ticketFromDb);
        return ticket;
    }
}
