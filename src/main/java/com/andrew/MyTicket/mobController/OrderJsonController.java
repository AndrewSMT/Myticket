package com.andrew.MyTicket.mobController;

import com.andrew.MyTicket.model.*;
import com.andrew.MyTicket.repositories.TicketRepo;
import com.andrew.MyTicket.service.MailSender;
import com.andrew.MyTicket.service.OrderService;
import com.andrew.MyTicket.service.PdfCreator;
import com.andrew.MyTicket.service.UserService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/mobile/tickets")
public class OrderJsonController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TicketRepo ticketRepo;

    @Autowired
    private PdfCreator pdfCreator;

    @GetMapping("/{id}")
    public List<Ticket> ticketlist(@PathVariable("id") Event event) {
        List<Ticket> list = ticketRepo.findTicketByEvent(event);
        return list;
    }

    @GetMapping("/order/{id}")
    public Orderr ticketAddInCart(@PathVariable("id") Ticket ticket, @RequestParam("order") Long orderId) {
        Orderr order = new Orderr();
        order.setId_order(orderId);
        if (orderId == 0L) {
            order.setId_order(orderService.genNumberForOrder());
        }
        order.setOrderStatus(Collections.singleton(OrderStatus.NOTPAYED));
        Set<TicketStatus> ticketStatuses = new HashSet<>();
        ticketStatuses.add(TicketStatus.BLOCKED);
        ticket.setTicketStatus(ticketStatuses);
        ticketRepo.save(ticket);
        order.getTicket().add(ticket);
        order.setTicket(order.getTicket());
        return order;
    }

    @GetMapping
    public String ticketOrder(@RequestParam("tickets") String tickets, @RequestParam("email") String email, @RequestParam("order") Long order) throws IOException, DocumentException, MessagingException {
        String fileName = (UUID.randomUUID().toString());
        Ticket ticket;
        Cart userCart = new Cart();
        User user = null;
        Orderr orderr = new Orderr();
        orderr.setId_order(order);
        for (String id : tickets.split(",")) {
            ticket = ticketRepo.findById(Long.valueOf(id)).get();
            Set<TicketStatus> ticketStatuses = new HashSet<>();
            ticketStatuses.add(TicketStatus.SOLD);
            ticket.setOrderNumber(order);
            ticket.setTicketStatus(ticketStatuses);
            ticketRepo.save(ticket);
            userCart.getTicket().add(ticket);
        }
        orderService.addFinishedOrder(userCart.getTicket(), user,orderr);
        pdfCreator.createPdfOrder(userCart, fileName);
        mailSender.sendPdf(email, "Tickets", fileName);
        return "true";
    }

    @GetMapping("/confirm")
    public String emailConfirm(@RequestParam("email") String email, @RequestParam("code") String code) {
        userService.sendConfirmEmail(email, code);
        return "true";
    }
    @GetMapping("/delete/{id}")
    public String deleteTicketFromCart(@PathVariable("id") Ticket ticket) {
        Set<TicketStatus> ticketStatuses = new HashSet<>();
        ticketStatuses.add(TicketStatus.ACTIVE);
        ticket.setTicketStatus(ticketStatuses);
        ticketRepo.save(ticket);
        return "true";
    }
}
