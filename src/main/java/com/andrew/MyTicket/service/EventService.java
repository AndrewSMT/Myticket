package com.andrew.MyTicket.service;

import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.repositories.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepo eventRepo;

    public Page<Event> findPaginated(Pageable pageable) {
        List<Event> events = eventRepo.findAll();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Event> list;

        if (events.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, events.size());
            list = events.subList(startItem, toIndex);
        }

        Page<Event> eventPage
                = new PageImpl<Event>(list, PageRequest.of(currentPage, pageSize), events.size());

        return eventPage;
    }
}
