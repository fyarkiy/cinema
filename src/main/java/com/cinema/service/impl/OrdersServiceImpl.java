package com.cinema.service.impl;

import com.cinema.dao.OrdersDao;
import com.cinema.lib.Inject;
import com.cinema.lib.Service;
import com.cinema.model.Order;
import com.cinema.model.Ticket;
import com.cinema.model.User;
import com.cinema.service.OrdersService;
import com.cinema.service.ShoppingCartService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Inject
    private OrdersDao ordersDao;
    @Inject
    private ShoppingCartService shoppingCartService;

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
