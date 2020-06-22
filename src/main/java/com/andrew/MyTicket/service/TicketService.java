package com.andrew.MyTicket.service;

import com.andrew.MyTicket.model.Ticket;
import com.andrew.MyTicket.model.TicketStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TicketService {
    public void saveUser(Ticket ticket) {
        Set<String> ticketStatus = Arrays.stream(TicketStatus.values())
                .map(TicketStatus::name)
                .collect(Collectors.toSet());

        ticket.getTicketStatus().clear();

    }
}
