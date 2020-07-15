package com.andrew.MyTicket.transfer;

import lombok.Data;

@Data
public class TicketFilterDto {
    private Long order;
    private String city;
    private String event;
    private String date;
}
