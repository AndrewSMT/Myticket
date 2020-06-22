package com.andrew.MyTicket.controller;

import com.andrew.MyTicket.model.*;
import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.repositories.CartRepo;
import com.andrew.MyTicket.repositories.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.List;

@SessionAttributes(value = "userCart")
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private TicketRepo ticketRep;



    @GetMapping("{id}")
    private String getTickets(@PathVariable("id") Event event, Model model) {
        model.addAttribute("tickets", ticketRep.findTicketByEvent(event));
        List<Ticket> tickets = ticketRep.findTicketByEvent(event);
        Set<Integer> setRow = new HashSet<>();
        for (Ticket t : tickets) {
            setRow.add(t.getRow_ticket());
        }
        model.addAttribute("setRow", setRow);
        return "order";
    }

    @GetMapping("/ticket/{id}")
    private String addTicketToCart(HttpSession httpSession, @PathVariable("id") Ticket ticket, @AuthenticationPrincipal User user, Model model) {
        ticket.getTicketStatus().clear();
        Set<TicketStatus> ticketStatuses = new HashSet<>();
        ticketStatuses.add(TicketStatus.BLOCKED);
        ticket.setTicketStatus(ticketStatuses);
        ticketRep.save(ticket);

        Cart cart = (Cart) httpSession.getAttribute("userCart");
        if (cart == null) {
            cart = new Cart();
            cart.setId_cart(user.getId_user());
        }

        cart.getTicket().add(ticket);
        cart.setTicket(cart.getTicket());
        httpSession.setAttribute("userCart", cart);
        return "redirect:/order/" + ticket.getEvent().getId();
    }

}
