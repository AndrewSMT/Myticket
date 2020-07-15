package com.andrew.MyTicket.model;


import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Cart {
    private Long id_cart;
    private Long id_order;
    private Set<Ticket> ticket = new HashSet<>();
}
