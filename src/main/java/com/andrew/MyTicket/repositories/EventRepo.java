package com.andrew.MyTicket.repositories;


import com.andrew.MyTicket.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<Event,Long> {
}
