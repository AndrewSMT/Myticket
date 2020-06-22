package com.andrew.MyTicket.repositories;


import com.andrew.MyTicket.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepo extends JpaRepository<Place,Long> {
}
