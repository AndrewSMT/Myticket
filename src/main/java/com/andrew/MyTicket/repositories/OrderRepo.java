package com.andrew.MyTicket.repositories;

import com.andrew.MyTicket.model.Orderr;
import com.andrew.MyTicket.model.Ticket;
import com.andrew.MyTicket.model.User;
import org.hibernate.criterion.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public interface OrderRepo extends JpaRepository<Orderr, Long> {
    List<Orderr> findByUser(User user);


}
