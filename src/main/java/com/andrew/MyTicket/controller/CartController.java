package com.andrew.MyTicket.controller;

import com.andrew.MyTicket.model.Cart;
import com.andrew.MyTicket.model.Orderr;
import com.andrew.MyTicket.model.Ticket;
import com.andrew.MyTicket.model.User;
import com.andrew.MyTicket.service.OrderService;
import com.andrew.MyTicket.service.TicketService;
import com.itextpdf.text.DocumentException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Controller of user cart requests
 *
 * @author Andreii Matveiev
 * @author andrei.matviev@gmail.com
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    /**
     * Declaration TicketService variable for dependency injection
     */
    private final TicketService ticketService;

    /**
     * Declaration OrderService variable for dependency injection
     */
    private final OrderService orderService;

    /**
     * Dependency injection into CartController with constructor
     *
     * @param ticketService Inject Ticket service
     * @param orderService  Inject Order service
     */
    public CartController(TicketService ticketService, OrderService orderService) {
        this.ticketService = ticketService;
        this.orderService = orderService;
    }

    /**
     * Open cart page
     * With a list of bought tickets
     *
     * @param user        Get current user
     * @param httpSession Get HttpSession as param
     * @param model       Model of page
     * @return String cart page
     */
    @GetMapping
    public String getCartPage(@AuthenticationPrincipal User user, HttpSession httpSession, Model model) {
        model.addAttribute("userCart", httpSession.getAttribute("userCart"));
        model.addAttribute("order", httpSession.getAttribute("order"));
        model.addAttribute(user);
        return "cart";
    }

    /**
     * Get request make payment for all tickets in cart
     *
     * @param user        Get current user
     * @param httpSession Get HttpSession as param
     * @param model       Model of page
     * @return String cart page
     * @throws IOException,MessagingException,DocumentException
     */
    @GetMapping("/pay")
    public String payCart(@AuthenticationPrincipal User user, HttpSession httpSession, Model model) throws IOException, DocumentException, MessagingException {
        String fileName = (UUID.randomUUID().toString());
        Cart userCart = (Cart) httpSession.getAttribute("userCart");
        Orderr order = (Orderr) httpSession.getAttribute("order");

        if (userCart != null) {
            Set<Ticket> tickets = orderService.makePayment(userCart, order, fileName, user);
            httpSession.removeAttribute("userCart");
            orderService.addFinishedOrder(tickets, user, order);
            model.addAttribute("userCart", httpSession.getAttribute("userCart"));
            model.addAttribute("success", "success");
        }

        return "cart";
    }

    /**
     * Delete selected ticket in cart
     *
     * @param httpSession Get HttpSession as param
     * @param ticket      Get ticket by  path variable
     * @return String redirect on cart page
     * @throws IOException,MessagingException,DocumentException
     */
    @GetMapping("/delete/{ticket}")
    public String deleteCartTicket(HttpSession httpSession, @PathVariable("ticket") Ticket ticket) {
        Cart userCart = (Cart) httpSession.getAttribute("userCart");
        Set<Ticket> tickets = userCart.getTicket();
        httpSession.removeAttribute("userCart");

        if (tickets.size() == 1) {
            httpSession.removeAttribute("order");
            ticketService.changeTicketStatus(ticket);
            httpSession.removeAttribute("userCart");
            return "redirect:/cart";
        }

        userCart = ticketService.cartDelete(ticket, userCart);

        httpSession.setAttribute("userCart", userCart);

        return "redirect:/cart";
    }

    /**
     * Clear tickets in cart
     *
     * @param httpSession Get HttpSession as param
     * @return String redirect on cart page
     * @throws IOException,MessagingException,DocumentException
     */
    @GetMapping("/clear")
    public String deleteAllCartTicket(HttpSession httpSession) {
        Cart userCart = (Cart) httpSession.getAttribute("userCart");
        Set<Ticket> tickets = userCart.getTicket();
        for (Ticket tc : tickets) {
            ticketService.changeTicketStatus(tc);
        }
        httpSession.removeAttribute("order");
        httpSession.removeAttribute("userCart");

        return "redirect:/cart";
    }
}
