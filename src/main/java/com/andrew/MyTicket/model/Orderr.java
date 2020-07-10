package com.andrew.MyTicket.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Orderr {
    @Id
    private Long id_order;
    @ElementCollection(targetClass = OrderStatus.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "order_status",joinColumns = @JoinColumn(name = "id_order"))
    private Set<OrderStatus> orderStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private User user;
    @ManyToMany
    @JoinTable(name="order_ticket", joinColumns = {@JoinColumn(name="id_order")},
            inverseJoinColumns = {@JoinColumn(name = "id_ticket")})
    private Set<Ticket> ticket = new HashSet<>();


}
