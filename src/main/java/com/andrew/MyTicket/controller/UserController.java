package com.andrew.MyTicket.controller;

import com.andrew.MyTicket.model.Orderr;
import com.andrew.MyTicket.model.Ticket;
import com.andrew.MyTicket.model.User;
import com.andrew.MyTicket.repositories.OrderRepo;
import com.andrew.MyTicket.repositories.UserRepo;
import com.andrew.MyTicket.service.UserService;
import com.andrew.MyTicket.transfer.TicketFilterDto;
import com.andrew.MyTicket.transfer.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * UserController of user request
 *
 * @author Andreii Matveiev
 * @author andrei.matviev@gmail.com
 */
@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('USER')")
public class UserController {

    /**
     * Declaration UserRepo variable for dependency injection
     */
    private final UserRepo userRepo;

    /**
     * Declaration UserService variable for dependency injection
     */
    private final UserService userService;

    /**
     * Declaration OrderRepo variable for dependency injection
     */
    private final OrderRepo orderRepo;

    /**
     * Dependency injection into UserController with constructor
     *
     * @param userRepo    Inject User repository
     * @param orderRepo   Inject Order repository
     * @param userService Inject User service
     */
    public UserController(UserRepo userRepo, OrderRepo orderRepo, UserService userService) {
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
        this.userService = userService;
    }

    /**
     * Open userProfile page
     *
     * @param user  Get current User
     * @param model Model of page
     * @return String userProfile page
     */
    @GetMapping("/userProfile")
    public String userEditPage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "userProfile";
    }


    /**
     * Edit user date
     *
     * @param userpic  Get User load picture
     * @param userLog Get current User
     * @param user Get new User data
     * @param model Model of page
     * @return String userProfile page
     */
    @PostMapping("/userProfile")
    public String userEdit(@RequestParam("userpic") MultipartFile userpic, @AuthenticationPrincipal User userLog, UserDto user, Model model) {
        boolean success = false;
        model.addAttribute("user", userLog);

        if (user.getUsername() != null) {
            if (user.getUsername().equals("")) {
                model.addAttribute("usernameError", "Username cannot be empty");
                return "userProfile";
            }
            success = true;
            userLog.setUsername(user.getUsername());
        }

        if (user.getEmail() != null) {
            if (user.getEmail().equals("")) {
                model.addAttribute("emailError", "Email cannot be empty");
                return "userProfile";
            }
            success = true;
            userLog.setEmail(user.getEmail());
            userService.sendEmail(userLog);
        }

        if (user.getPassword() != null) {
            if (user.getPassword().equals("")) {
                model.addAttribute("passwordError", "Password cannot be empty");
                return "userProfile";
            }
            if (user.getPassword2().equals("")) {
                model.addAttribute("password2Error", "Password confirmation cannot be empty");
                return "userProfile";
            }
            if (!user.getPassword().equals(user.getPassword2())) {
                model.addAttribute("passwordError", "Password are different");
                return "userProfile";
            }
            success = true;
            userLog.setPassword(user.getPassword());
        }

        if (userpic != null) {
            if (!userpic.getOriginalFilename().equals("")) {
                userLog.setPicture("http://localhost:8080/picture/" + userpic.getOriginalFilename());
            }
        }

        userService.editUser(userLog);
        model.addAttribute("success", success);
        model.addAttribute("user", userLog);

        return "userProfile";
    }

    /**
     * Open userTickets page with list of all user tickets
     *
     * @param user Get current User data
     * @param model Model of page
     * @return String userTickets page
     */
    @GetMapping("/userTickets")
    public String userTickets(@AuthenticationPrincipal User user, Model model) {
        List<Orderr> listOrder = orderRepo.findByUser(user);
        List<Ticket> listTicket = new ArrayList<>();

        for (Orderr or : listOrder) {
            listTicket.addAll(or.getTicket());
        }

        if (!listTicket.isEmpty()) {
            model.addAttribute("tickets", listTicket);
        }

        model.addAttribute("orders", listOrder);

        return "userTickets";
    }

    /**
     * Filter tickets of current User
     *
     * @param filter Get filter param
     * @param user Get current User data
     * @param model Model of page
     * @return String userTickets page
     */
    @GetMapping("/userTickets/filter/{param}")
    public String userTicketsFilter(@PathVariable("param") String filter, @AuthenticationPrincipal User user, Model model) {
        List<Orderr> listOrder = orderRepo.findByUser(user);
        List<Ticket> listTicket = new ArrayList<>();
        for (Orderr or : listOrder) {
            listTicket.addAll(or.getTicket());
        }
        switch (filter) {
            case ("event"):
                listTicket.sort(Ticket::compareToByEvent);
                break;
            case ("city"):
                listTicket.sort(Ticket::compareToByCity);
                break;
            case ("date"):
                listTicket.sort(Ticket::compareToByDate);
                break;
            case ("order"):
                listTicket.sort(Ticket::compareToByOrder);
                break;
        }

        model.addAttribute("tickets", listTicket);
        model.addAttribute("orders", listOrder);

        return "userTickets";
    }

    /**
     * Filter tickets of current User
     *
     * @param filterForm Get filterForm param
     * @param user Get current User data
     * @param model Model of page
     * @return String userTickets page
     */
    @GetMapping("/userTickets/search")
    public String userTicketsFilter(TicketFilterDto filterForm, @AuthenticationPrincipal User user, Model model) {
        List<Orderr> listOrder = orderRepo.findByUser(user);
        Set<Ticket> listTicket = Collections.newSetFromMap(new ConcurrentHashMap<>());

        for (Orderr or : listOrder) {
            listTicket.addAll(or.getTicket());
        }

        if (!filterForm.getCity().isEmpty()) {
            for (Ticket tc : listTicket) {
                if (!tc.getEvent().getPlace().getCity().getTitle().equals(filterForm.getCity())) {
                    listTicket.remove(tc);
                }
            }
        }

        if (!filterForm.getEvent().isEmpty()) {
            for (Ticket tc : listTicket) {
                if (!tc.getEvent().getTitle().equals(filterForm.getEvent())) {
                    listTicket.remove(tc);
                }
            }
        }

        if (filterForm.getOrder() != null) {
            for (Ticket tc : listTicket) {
                if (!tc.getOrderNumber().equals(filterForm.getOrder())) {
                    listTicket.remove(tc);
                }
            }
        }

        if (!filterForm.getDate().isEmpty()) {
            String date = filterForm.getDate().substring(0, 10) + ", " + filterForm.getDate().substring(11, 16);
            for (Ticket tc : listTicket) {
                if (!tc.getEvent().getDate().equals(date)) {
                    listTicket.remove(tc);
                }
            }
        }

        model.addAttribute("tickets", listTicket);
        model.addAttribute("orders", listOrder);

        return "userTickets";
    }
    /**
     * Open userOrders page
     *
     * @param user  Get current User
     * @param model Model of page
     * @return String userProfile page
     */
    @GetMapping("/userOrders")
    public String userOrders(@AuthenticationPrincipal User user, Model model) {
        List<Orderr> listOrder = orderRepo.findByUser(user);
        model.addAttribute("orders", listOrder);
        return "userOrders";
    }

    /**
     * Open userProfile page
     *
     * @param model Model of page
     * @return String userProfile page
     */
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "adminFunc";
    }

    /**
     * Open userProfile page
     *
     * @param userFromDb  Get current User
     * @param user Get new User data
     * @return String adminFunc page
     */
    @PutMapping("{id}")
    public String update(@PathVariable("id") User userFromDb, @RequestBody User user) {
        BeanUtils.copyProperties(user, userFromDb, "id");
        userRepo.save(userFromDb);
        return "adminFunc";
    }
}

