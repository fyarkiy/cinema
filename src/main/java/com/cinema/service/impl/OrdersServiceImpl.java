package com.cinema.service.impl;

import com.cinema.dao.OrdersDao;
import com.cinema.model.Order;
import com.cinema.model.Ticket;
import com.cinema.model.User;
import com.cinema.service.OrdersService;
import com.cinema.service.ShoppingCartService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrdersServiceImpl implements OrdersService {
    private final OrdersDao ordersDao;
    private final ShoppingCartService shoppingCartService;

    public OrdersServiceImpl(OrdersDao ordersDao, ShoppingCartService shoppingCartService) {
        this.ordersDao = ordersDao;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public Order completeOrder(List<Ticket> tickets, User user) {
        Order order = ordersDao.add(new Order(new ArrayList<Ticket>(tickets),
                LocalDateTime.now(), user));
        shoppingCartService.clear(shoppingCartService.getByUser(user));
        return order;
    }

    @Override
    public List<Order> getOrderHistory(User user) {
        return ordersDao.getOrderHistory(user);
    }
}
