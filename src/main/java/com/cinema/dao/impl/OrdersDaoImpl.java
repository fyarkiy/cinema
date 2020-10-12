package com.cinema.dao.impl;

import com.cinema.dao.OrdersDao;
import com.cinema.exception.DataProcessingException;
import com.cinema.lib.Dao;
import com.cinema.model.Orders;
import com.cinema.model.User;
import com.cinema.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class OrdersDaoImpl implements OrdersDao {

    @Override
    public Orders add(Orders order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(order);
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
    public List<Orders> getOrderHistory(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Orders> ordersQuery = session.createQuery("from Orders o "
                    + " join fetch o.tickets t "
                    + " join fetch t.movieSession movieSession "
                    + " join fetch t.movieSession.movie movie "
                    + " join fetch t.movieSession.cinemaHall cinemaHall "
                    + " join fetch o.user where o.user =: user", Orders.class);
            ordersQuery.setParameter("user", user);
            return ordersQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get all orders for user " + user, e);
        }
    }
}
