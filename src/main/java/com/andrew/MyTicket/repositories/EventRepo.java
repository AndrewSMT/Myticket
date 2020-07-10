package com.andrew.MyTicket.repositories;


import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.model.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepo extends JpaRepository<Event,Long> {
    Event findByTitleAndDate(String title, String date);
    List<Event> findEventsByPlace(Place place);
}
