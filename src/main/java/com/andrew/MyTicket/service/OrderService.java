package com.andrew.MyTicket.service;

import com.andrew.MyTicket.model.*;
import com.andrew.MyTicket.repositories.OrderRepo;
import com.andrew.MyTicket.repositories.TicketRepo;
import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class OrderService {
    private final static int MIN = 100000;
    private final static int MAX = 999999999;
    private final static int DIFF = MAX - MIN;

    /**
     * Declaration MailSender variable for dependency injection
     */
    private final MailSender mailSender;

    /**
     * Declaration PdfCreator variable for dependency injection
     */
    private final PdfCreator pdfCreator;
    /**
     * Declaration TicketRepo variable for dependency injection
     */
    private final TicketRepo ticketRepo;

    /**
     * Declaration OrderRepo variable for dependency injection
     */
    private final OrderRepo orderRepo;

    public OrderService(MailSender mailSender, PdfCreator pdfCreator, TicketRepo ticketRepo, OrderRepo orderRepo) {
        this.mailSender = mailSender;
        this.pdfCreator = pdfCreator;
        this.ticketRepo = ticketRepo;
        this.orderRepo = orderRepo;
    }

    public boolean addFinishedOrder(Set<Ticket> tickets, User user, Orderr orderr) {
        Orderr order = new Orderr();
        order.setOrderStatus(Collections.singleton(OrderStatus.FINISHED));
        order.setTicket(tickets);
        order.setUser(user);
        order.setId_order(orderr.getId_order());
        orderRepo.save(order);
        return true;
    }

    public Long genNumberForOrder() {
        Random random = new Random();
        int randomNum = random.nextInt(DIFF + 1);
        randomNum += MIN;
        Long genNumber = Long.valueOf((randomNum));
        if (orderRepo.findById(genNumber).isPresent()) {
            genNumberForOrder();
        }
        return genNumber;
    }

    public Set<Ticket> makePayment(Cart userCart, Orderr order, String fileName, User user) throws IOException, DocumentException, MessagingException {
        Set<Ticket> tickets = userCart.getTicket();
        for (Ticket t : tickets) {
            Set<TicketStatus> ticketStatuses = new HashSet<>();
            ticketStatuses.add(TicketStatus.SOLD);
            t.setOrderNumber(order.getId_order());
            t.setTicketStatus(ticketStatuses);
            ticketRepo.save(t);
            order.setOrderStatus(Collections.singleton(OrderStatus.FINISHED));
        }
        pdfCreator.createPdfOrder(userCart, fileName);
        mailSender.sendPdf(user.getEmail(), "Tickets", fileName);
        return tickets;
    }

    public void startTicketTimer(HttpSession httpSession, Model model, User user) {
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
                        ticketRepo.save(ticket);
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
