package com.andrew.MyTicket.repositories;


import com.andrew.MyTicket.model.City;
import com.andrew.MyTicket.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepo extends JpaRepository<Place,Long> {
    List<Place> findPlacesByCity(City city);
}
