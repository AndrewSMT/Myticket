package com.andrew.MyTicket.service;

import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.model.Role;
import com.andrew.MyTicket.model.User;
import com.andrew.MyTicket.repositories.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

@Service
public class AdminService {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private EventRepo eventRepo;

    public boolean addEvent(Event event, MultipartFile file) throws IOException {
        Event eventFromDb = eventRepo.findByTitleAndAndDate(event.getTitle(),event.getDate());
        if(eventFromDb != null){
            return false;
        }
        String date = event.getDate().substring(0,10) +", "+ event.getDate().substring(11,16);
        event.setDate(date);
        if(event.getPicture() != null) {
        file.transferTo(new File(uploadPath  +"/" + file.getOriginalFilename()));
            event.setPicture("http://localhost:8080/picture/" + file.getOriginalFilename());
        }
        eventRepo.save(event);
        return true;
    }

}
