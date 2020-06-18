package com.andrew.MyTicket.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_cart;
    private Long id_order;
    @ManyToMany
    @JoinTable(name="cart_ticket", joinColumns = {@JoinColumn(name="id_cart")},
            inverseJoinColumns = {@JoinColumn(name = "id_ticket")})
    private Set<Ticket> ticket = new HashSet<>();

}
