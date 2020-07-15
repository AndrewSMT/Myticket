package com.andrew.MyTicket.service;

import com.andrew.MyTicket.model.OrderStatus;
import com.andrew.MyTicket.model.Orderr;
import com.andrew.MyTicket.model.Ticket;
import com.andrew.MyTicket.model.User;
import com.andrew.MyTicket.repositories.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Random;
import java.util.Set;

@Service
public class OrderService {
    private final static int  MIN = 100000;
    private final static int  MAX = 999999999;
    private final static int  DIFF = MAX - MIN ;

    @Autowired
    private OrderRepo orderRepo;

    public boolean addOrderFinished(Set<Ticket> tickets, User user,Orderr orderr){
        Orderr order = new Orderr();
        order.setOrderStatus(Collections.singleton(OrderStatus.FINISHED));
        order.setTicket(tickets);
        order.setUser(user);
        order.setId_order(orderr.getId_order());
        orderRepo.save(order);
        return true;
    }

    public Long genNumberForOrder(){
        Random random = new Random();
        int randomNum = random.nextInt(DIFF + 1);
        randomNum += MIN;
        Long genNumber = Long.valueOf((randomNum));
        if (orderRepo.findById(genNumber).isPresent()){
            genNumberForOrder();
        }
        return genNumber;
    }
}
