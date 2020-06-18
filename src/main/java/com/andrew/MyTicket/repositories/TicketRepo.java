package com.andrew.MyTicket.repositories;

import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.model.Ticket;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepo extends JpaRepository<Ticket,Long> {
    <S extends Ticket> List<S> findTicketByEvent(Event event);
}
