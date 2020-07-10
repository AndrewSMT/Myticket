package com.andrew.MyTicket.repositories;

import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.model.Ticket;
import com.andrew.MyTicket.model.TicketStatus;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface TicketRepo extends JpaRepository<Ticket, Long> {
    <S extends Ticket> List<S> findTicketByEvent(Event event);
   Ticket findTicketByRowAndNumber(Integer row, Integer number);
   Ticket findTicketByRowAndNumberAndEvent(Integer row, Integer number,Event event);
   @Query("select MIN(price) from Ticket where event = :event")
   Integer findTicketByMinPrice(@Param("event") Event event);
    @Query("select MAX(price) from Ticket where event = :event")
    Integer findTicketByMaxPrice(@Param("event") Event event);
    @Query("Select count(ticket) from Ticket ticket where event = :event")
    Integer findTicketCount(@Param("event") Event event);

    List<Ticket> findTicketsByEventAndTicketStatus(Event event,TicketStatus ticketStatus);

}
