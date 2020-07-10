package com.andrew.MyTicket.dto;

import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.model.Ticket;
import com.andrew.MyTicket.model.TicketStatus;
import lombok.Data;

import java.util.Collections;

@Data
public class AddTicketsDto {
    private String title;
    private Integer row_ticket_from;
    private Integer row_ticket_to;
    private String date;
    private Integer place1;
    private Integer place2;
    private Integer price;

    public Ticket ticketBuilder(AddTicketsDto addTicketsDto, Event event, Integer row, Integer number) {
        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setTicketStatus(Collections.singleton(TicketStatus.ACTIVE));
        ticket.setNumber(number);
        ticket.setPrice(addTicketsDto.price);
        ticket.setRow(row);
        return ticket;
    }
}
