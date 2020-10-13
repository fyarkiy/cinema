package com.cinema.dao.impl;

import com.cinema.dao.OrdersDao;
import com.cinema.exception.DataProcessingException;
import com.cinema.lib.Dao;
import com.cinema.model.Order;
import com.cinema.model.User;
import com.cinema.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class OrdersDaoImpl implements OrdersDao {
    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> ordersQuery = session.createQuery("SELECT DISTINCT o FROM Order o "
                    + " JOIN FETCH o.tickets t "
                    + " JOIN o.user WHERE o.user =: user", Order.class);
            ordersQuery.setParameter("user", user);
            return ordersQuery.getResultList();
        }
    }
}
