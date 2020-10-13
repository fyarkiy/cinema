package com.cinema.dao;

import com.cinema.model.Order;
import com.cinema.model.User;
import java.util.List;

public interface OrdersDao {
    Order add(Order order);

    List<Order> getOrderHistory(User user);
}
