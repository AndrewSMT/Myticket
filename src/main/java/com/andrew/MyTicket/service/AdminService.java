package com.andrew.MyTicket.service;

import com.andrew.MyTicket.transfer.AddTicketsDto;
import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.model.Ticket;
import com.andrew.MyTicket.repositories.EventRepo;
import com.andrew.MyTicket.repositories.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private EventRepo eventRepo;


    @Autowired
    private TicketRepo ticketRepo;

    public boolean addEvent(Event event, MultipartFile file) throws IOException {
            String date = event.getDate().substring(0, 10) + ", " + event.getDate().substring(11, 16);
            Event eventFromDb = eventRepo.findByTitleAndDate(event.getTitle(), date);
            if (eventFromDb != null) {
                return false;
            }

            event.setDate(date);
        if (!file.getOriginalFilename().equals("")) {
            event.setPicture("http://localhost:8080/picture/" + file.getOriginalFilename());
        }
            eventRepo.save(event);
        return true;
    }

    public Map<String, Boolean> addTickets(AddTicketsDto addTicketsDto) {
        Map<String, Boolean> ticketMap = new HashMap<>();

        String date = addTicketsDto.getDate().substring(0, 10) + ", " + addTicketsDto.getDate().substring(11, 16);
        Event eventFromDb = eventRepo.findByTitleAndDate(addTicketsDto.getTitle(), date);

        if (eventFromDb == null) {
            ticketMap.put("Event not exist", false);
            return ticketMap;
        }
        if (addTicketsDto.getRow_ticket_from() > addTicketsDto.getRow_ticket_to()) {
            ticketMap.put("Row from can't be bigger than row to", false);
            return ticketMap;
        }

        if (addTicketsDto.getPlace1() > addTicketsDto.getPlace2()) {
            ticketMap.put("Place from can't be bigger than place to", false);
            return ticketMap;
        }


        for (int i = addTicketsDto.getRow_ticket_from(); i <= addTicketsDto.getRow_ticket_to(); i++) {
            for (int j = addTicketsDto.getPlace1(); j <= addTicketsDto.getPlace2(); j++) {
                if (ticketRepo.findTicketByRowAndNumberAndEvent(i, j,eventFromDb) == null) {
                    Ticket ticket = addTicketsDto.ticketBuilder(addTicketsDto, eventFromDb, i, j);
                    ticketRepo.save(ticket);
                } else {
                    ticketMap.put( "Tickets already exist", false);
                    break;
                }
            }
        }
        return ticketMap;
    }
}
