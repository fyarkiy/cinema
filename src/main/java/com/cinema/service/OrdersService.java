package com.cinema.service;

import com.cinema.model.Orders;
import com.cinema.model.Ticket;
import com.cinema.model.User;
import java.util.List;

public interface OrdersService {
    Orders completeOrder(List<Ticket> tickets, User user);

    List<Orders> getOrderHistory(User user);
}
