package com.andrew.MyTicket.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_place;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_city")
    private City city;
    private String title;
}
