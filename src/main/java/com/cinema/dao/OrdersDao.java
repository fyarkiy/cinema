package com.cinema.dao;

import com.cinema.model.Orders;
import com.cinema.model.User;
import java.util.List;

public interface OrdersDao {
    Orders add(Orders order);

    List<Orders> getOrderHistory(User user);
}
