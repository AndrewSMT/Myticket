package com.andrew.MyTicket.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_ticket;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_event")
    private Event event;
    private Integer row_ticket;
    private Integer number;
    private Integer price;
    @ElementCollection(targetClass = TicketStatus.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "ticket_status",joinColumns = @JoinColumn(name = "id_ticket"))
    private Set<TicketStatus> ticketStatus;
}
