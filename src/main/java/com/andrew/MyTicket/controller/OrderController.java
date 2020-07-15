package com.andrew.MyTicket.controller;

import com.andrew.MyTicket.model.*;
import com.andrew.MyTicket.repositories.TicketRepo;
import com.andrew.MyTicket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SessionAttributes({"userCart", "order"})

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private TicketRepo ticketRep;

    @Autowired
    private OrderService orderService;


    @GetMapping("{id}")
    private String getTickets(@AuthenticationPrincipal User user, @PathVariable("id") Event event, HttpSession httpSession, Model model) {
        List<Ticket> tickets = ticketRep.findTicketByEvent(event);
        Set<Integer> setRow = new HashSet<>();
        for (Ticket t : tickets) {
            setRow.add(t.getRow());
        }
        Cart cart = (Cart) httpSession.getAttribute("userCart");
        if (cart != null) {
            for (Ticket ticket : tickets) {
                for (Ticket cartT : cart.getTicket()) {
                    if (ticket.getId_ticket().equals(cartT.getId_ticket())) {
                        ticket.setUserCart(user);
                        break;
                    } else {
                        ticket.setUserCart(new User());
                    }
                }
            }
        }
        model.addAttribute("setRow", setRow);
        model.addAttribute("user", user);
        model.addAttribute("tickets", tickets);
        return "order";
    }

    @GetMapping("/ticket/{id}")
    private String addTicketToCart(HttpSession httpSession, @PathVariable("id") Ticket ticket, @AuthenticationPrincipal User user, Model model) {
        Orderr order = (Orderr) httpSession.getAttribute("order");
        Cart cart = (Cart) httpSession.getAttribute("userCart");
        ticket.getTicketStatus().clear();
        Set<TicketStatus> ticketStatuses = new HashSet<>();
        ticketStatuses.add(TicketStatus.BLOCKED);
        ticket.setTicketStatus(ticketStatuses);
        ticketRep.save(ticket);

        if (cart == null) {
            cart = new Cart();
            cart.setId_cart(user.getId_user());
        }

        if (order == null) {
            order = new Orderr();
            order.setId_order(orderService.genNumberForOrder());
            order.setUser(user);
            order.setOrderStatus(Collections.singleton(OrderStatus.NOTPAYED));
        }

        cart.getTicket().add(ticket);
        cart.setTicket(cart.getTicket());

        order.getTicket().add(ticket);
        order.setTicket(order.getTicket());

        httpSession.setAttribute("userCart", cart);
        httpSession.setAttribute("order", order);
        return "redirect:/order/" + ticket.getEvent().getId();
    }

    @GetMapping("/ticket/timeout")
    private void orderTimeOut(@AuthenticationPrincipal User user, HttpSession httpSession, Model model) {
        Runnable run = () -> {
            try {
                Thread.sleep(900000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Cart cart = (Cart) httpSession.getAttribute("userCart");
            if (cart.getTicket() != null) {
                Set<Ticket> tickets = cart.getTicket();
                for (Ticket ticket : tickets) {
                    if (ticket.getTicketStatus().contains(TicketStatus.BLOCKED)) {
                        ticket.setTicketStatus(Collections.singleton(TicketStatus.ACTIVE));
                        ticketRep.save(ticket);
                    }
                }
            }
            httpSession.removeAttribute("userCart");
            model.addAttribute("userCart", httpSession.getAttribute("userCart"));
        };
        Thread myThread = new Thread(run, user.getUsername());
        myThread.start();
    }
}



