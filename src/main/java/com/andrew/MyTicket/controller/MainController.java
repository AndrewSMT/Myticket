package com.andrew.MyTicket.controller;

import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.model.Place;
import com.andrew.MyTicket.repositories.CityRepo;
import com.andrew.MyTicket.repositories.EventRepo;
import com.andrew.MyTicket.repositories.PlaceRepo;
import com.andrew.MyTicket.service.EventService;
import com.andrew.MyTicket.service.PdfCreator;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class MainController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private PlaceRepo placeRepo;

    @Autowired
    private CityRepo cityRepo;

    @Autowired
    private EventService eventService;

    @GetMapping
    public String list(Model model, @RequestParam("page") Optional<Integer> page,
                       @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(12);

        Page<Event> eventPage = eventService.findPaginated(PageRequest.of(currentPage - 1, pageSize));


        int totalPages = eventPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("events", eventPage);
        return "main";
    }

    // Login form
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/main/{city}")
    public String list(@PathVariable("city") String city, Model model) {
        List<Place> places = placeRepo.findPlacesByCity(cityRepo.findCityByTitle(city));
        List<Event> events = new ArrayList<>();
        for (Place pl : places) {
            events.addAll(eventRepo.findEventsByPlace(pl));
        }
        if (events.isEmpty()) {
            model.addAttribute("message", "Events not found");
        }
        model.addAttribute("events", events);
        return "main";
    }

    @GetMapping("/main/search")
    public String search(@RequestParam("searchText") String searchText, Model model) {
        List<Event> events = new ArrayList<>();
        Pattern pattern = Pattern.compile(searchText.toLowerCase());
        Matcher matcher;
        for (Event event : eventRepo.findAll()) {
            matcher = pattern.matcher(event.getTitle().toLowerCase());
            while (matcher.find()) {
                events.add(event);
            }
        }
        model.addAttribute("message", "Found: " + events.size() + " events");
        model.addAttribute("searchText", searchText);
        model.addAttribute("events", events);
        return "main";
    }
}
