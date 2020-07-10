package com.andrew.MyTicket.model;

import lombok.Data;
import org.springframework.format.datetime.standard.DateTimeContext;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    @Column(name = "row_ticket")
    private Integer row;
    private Integer number;
    private Integer price;
    @ElementCollection(targetClass = TicketStatus.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "ticket_status",joinColumns = @JoinColumn(name = "id_ticket"))
    private Set<TicketStatus> ticketStatus;
    @Transient
    private User userCart;
    private Long orderNumber;

    public int compareToByEvent(Ticket t){
        return event.getTitle().compareTo(t.getEvent().getTitle());
    }
    public int compareToByCity(Ticket t){ return event.getPlace().getCity().getTitle().compareTo(t.event.getPlace().getCity().getTitle());}
    public int compareToByDate(Ticket t){
        return event.getDate().compareTo(t.getEvent().getDate());
    }
    public int compareToByOrder(Ticket t){
        return orderNumber.compareTo(t.orderNumber);
    }
}
