package com.andrew.MyTicket.controller;

import com.andrew.MyTicket.model.Cart;
import com.andrew.MyTicket.model.Ticket;
import com.andrew.MyTicket.model.TicketStatus;
import com.andrew.MyTicket.model.User;
import com.andrew.MyTicket.repositories.TicketRepo;
import com.andrew.MyTicket.service.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private TicketRepo ticketRep;

    @GetMapping
    public String getCartPage(HttpSession httpSession, Model model) {
        model.addAttribute("userCart", httpSession.getAttribute("userCart"));
        return "cart";
    }

    @GetMapping("/pay")
    public String payCart(HttpSession httpSession, Model model, @AuthenticationPrincipal User user) {
        Cart userCart = (Cart) httpSession.getAttribute("userCart");
        Set<Ticket> tickets = userCart.getTicket();
        if(tickets != null) {
            for (Ticket t : tickets) {
                Set<TicketStatus> ticketStatuses = new HashSet<>();
                ticketStatuses.add(TicketStatus.SOLD);
                t.setTicketStatus(ticketStatuses);
                ticketRep.save(t);
            }

            mailSender.send(user.getEmail(), "Tickets", tickets.toString());
            httpSession.removeAttribute("userCart");

            model.addAttribute("userCart", httpSession.getAttribute("userCart"));
        }

        return "cart";
    }
}
