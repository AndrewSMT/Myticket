package com.andrew.MyTicket.repositories;

import com.andrew.MyTicket.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart,Long> {
}
