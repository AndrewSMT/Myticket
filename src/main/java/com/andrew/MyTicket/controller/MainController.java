package com.andrew.MyTicket.controller;

import com.andrew.MyTicket.model.Event;
import com.andrew.MyTicket.model.Place;
import com.andrew.MyTicket.repositories.CityRepo;
import com.andrew.MyTicket.repositories.EventRepo;
import com.andrew.MyTicket.repositories.PlaceRepo;
import com.andrew.MyTicket.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * MainController for requests on main page
 *
 * @author Andreii Matveiev
 * @author andrei.matviev@gmail.com
 */
@Controller
public class MainController {

    /**
     * Declaration EventRepo variable for dependency injection
     */
    private final EventRepo eventRepo;

    /**
     * Declaration PlaceRepo variable for dependency injection
     */
    private final PlaceRepo placeRepo;

    /**
     * Declaration CityRepo variable for dependency injection
     */
    private final CityRepo cityRepo;

    /**
     * Declaration EventService variable for dependency injection
     */
    private final EventService eventService;

    /**
     * Dependency injection into MainController with constructor
     *
     * @param eventRepo    Inject Event repository
     * @param placeRepo    Inject Place repository
     * @param cityRepo     Inject City repository
     * @param eventService Inject Event service
     */
    public MainController(EventRepo eventRepo, PlaceRepo placeRepo, CityRepo cityRepo, EventService eventService) {
        this.eventRepo = eventRepo;
        this.placeRepo = placeRepo;
        this.cityRepo = cityRepo;
        this.eventService = eventService;
    }

    /**
     * Open main page with events
     *
     * @param model Model of page
     * @param page  Param needed for pagination
     * @param size  Size elements on page
     * @return String main page
     */
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

    /**
     * Open login page
     *
     * @return String name of template
     */
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Open main page with events sorted by city
     *
     * @param city Get city by path variable id
     * @param model Model of page
     * @return String main page
     */
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

    /**
     * Open main page with search request
     *
     * @param searchText Get event by text param
     * @param model Model of page
     * @return String main page
     */
    @GetMapping("/main/search")
    public String search(@RequestParam("searchText") String searchText, Model model) {
        Matcher matcher;
        List<Event> events = new ArrayList<>();
        Pattern pattern = Pattern.compile(searchText.toLowerCase());

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
