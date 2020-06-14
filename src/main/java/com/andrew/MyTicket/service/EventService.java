package com.andrew.MyTicket.service;

import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.repositories.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    private EventRepo eventRepo;


}
