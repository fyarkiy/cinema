package com.cinema.dao.impl;

import com.cinema.dao.OrdersDao;
import com.cinema.exception.DataProcessingException;
import com.cinema.model.Order;
import com.cinema.model.User;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class OrdersDaoImpl implements OrdersDao {
    private SessionFactory sessionFactory;

    public OrdersDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't create order for user " + order.getUser(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getOrderHistory(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<Order> ordersQuery = session.createQuery("SELECT DISTINCT o FROM Order o "
                    + " JOIN FETCH o.tickets t "
                    + " JOIN o.user WHERE o.user =: user", Order.class);
            ordersQuery.setParameter("user", user);
            return ordersQuery.getResultList();
        }
    }
}
