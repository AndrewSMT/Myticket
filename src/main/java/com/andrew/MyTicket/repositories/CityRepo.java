package com.andrew.MyTicket.repositories;

import com.andrew.MyTicket.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepo extends JpaRepository<City,Long> {
    City findCityByTitle(String string);
}
