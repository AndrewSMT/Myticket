package com.andrew.MyTicket.controller;

import com.andrew.MyTicket.model.*;
import com.andrew.MyTicket.repositories.TicketRepo;
import com.andrew.MyTicket.service.OrderService;
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

/**
 * OrderController for order requests
 *
 * @author Andreii Matveiev
 * @author andrei.matviev@gmail.com
 */
@SessionAttributes({"userCart", "order"})
@Controller
@RequestMapping("/order")
public class OrderController {

    /**
     * Declaration TicketRepo variable for dependency injection
     */
    private final TicketRepo ticketRep;

    /**
     * Declaration OrderService variable for dependency injection
     */
    private final OrderService orderService;

    /**
     * Dependency injection into OrderController with constructor
     *
     * @param ticketRep    Inject Ticket repository
     * @param orderService Inject Order service
     */
    public OrderController(TicketRepo ticketRep, OrderService orderService) {
        this.ticketRep = ticketRep;
        this.orderService = orderService;
    }

    /**
     * Open order page with tickets of selected event
     *
     * @param user        Get current User
     * @param event       Get Event by path variable id
     * @param httpSession Get HttpSession as param
     * @param model       Model of page
     * @return String order page
     */
    @GetMapping("{id}")
    private String getTickets(@AuthenticationPrincipal User user, @PathVariable("id") Event event, HttpSession httpSession, Model model) {
        List<Ticket> tickets = ticketRep.findTicketByEvent(event);
        Cart cart = (Cart) httpSession.getAttribute("userCart");
        Set<Integer> setRow = new HashSet<>();

        for (Ticket t : tickets) {
            setRow.add(t.getRow());
        }

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

    /**
     * Add selected ticket in cart of current user
     *
     * @param user        Get current User
     * @param ticket      Get Ticket by path variable id
     * @param httpSession Get HttpSession as param
     * @return String order on cart page
     */
    @GetMapping("/ticket/{id}")
    private String addTicketToCart(@AuthenticationPrincipal User user, @PathVariable("id") Ticket ticket, HttpSession httpSession) {
        Orderr order = (Orderr) httpSession.getAttribute("order");
        Cart cart = (Cart) httpSession.getAttribute("userCart");
        Set<TicketStatus> ticketStatuses = new HashSet<>();

        ticket.getTicketStatus().clear();
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

    /**
     * After adding a ticket to the cart, starts the ticket purchase timer
     *
     * @param user        Get current User
     * @param httpSession Get HttpSession as param
     * @param model       Model of page
     */
    @GetMapping("/ticket/timeout")
    private void orderTimeOut(@AuthenticationPrincipal User user, HttpSession httpSession, Model model) {
       orderService.startTicketTimer(httpSession,model,user);
    }
}



