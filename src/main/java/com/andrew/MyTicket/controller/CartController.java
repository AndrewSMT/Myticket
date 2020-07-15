package com.andrew.MyTicket.controller;

import com.andrew.MyTicket.model.*;
import com.andrew.MyTicket.repositories.OrderRepo;
import com.andrew.MyTicket.repositories.TicketRepo;
import com.andrew.MyTicket.service.MailSender;
import com.andrew.MyTicket.service.OrderService;
import com.andrew.MyTicket.service.PdfCreator;
import com.andrew.MyTicket.service.TicketService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.persistence.criteria.Order;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private TicketRepo ticketRep;


    @Autowired
    private TicketService ticketService;

    @Autowired
    private PdfCreator pdfCreator;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String getCartPage(@AuthenticationPrincipal User user, HttpSession httpSession, Model model) {
        model.addAttribute("userCart", httpSession.getAttribute("userCart"));
        model.addAttribute("order", httpSession.getAttribute("order"));
        model.addAttribute(user);
        return "cart";
    }

    @GetMapping("/pay")
    public String payCart(HttpSession httpSession, Model model, @AuthenticationPrincipal User user) throws IOException, MessagingException, DocumentException {
        String fileName = (UUID.randomUUID().toString());
        Cart userCart = (Cart) httpSession.getAttribute("userCart");
        Orderr order = (Orderr) httpSession.getAttribute("order");
        if (userCart != null) {
            Set<Ticket> tickets = userCart.getTicket();
            for (Ticket t : tickets) {
                Set<TicketStatus> ticketStatuses = new HashSet<>();
                ticketStatuses.add(TicketStatus.SOLD);
                t.setOrderNumber(order.getId_order());
                t.setTicketStatus(ticketStatuses);
                ticketRep.save(t);
                order.setOrderStatus(Collections.singleton(OrderStatus.FINISHED));
            }
            pdfCreator.createPdfOrder(userCart,fileName);
            mailSender.sendPdf(user.getEmail(), "Tickets",fileName);
            httpSession.removeAttribute("userCart");

            orderService.addOrderFinished(tickets, user,order);

            model.addAttribute("userCart", httpSession.getAttribute("userCart"));
            model.addAttribute("success", "success");
        }
        return "cart";
    }

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

    @GetMapping("/clear")
    public String deleteAllCartTicket(HttpSession httpSession) {
        Cart userCart = (Cart) httpSession.getAttribute("userCart");
        Set<Ticket> tickets = userCart.getTicket();
        for(Ticket tc: tickets){
            ticketService.changeTicketStatus(tc);
        }
        httpSession.removeAttribute("order");
        httpSession.removeAttribute("userCart");
        return "redirect:/cart";
    }
}
