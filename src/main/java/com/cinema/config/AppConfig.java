package com.cinema.config;

import javax.sql.DataSource;

import com.cinema.model.CinemaHall;
import com.cinema.model.Movie;
import com.cinema.model.MovieSession;
import com.cinema.model.Order;
import com.cinema.model.ShoppingCart;
import com.cinema.model.Ticket;
import com.cinema.model.User;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import java.util.Properties;

@Configuration
@PropertySource("classpath:db.properties")
@ComponentScan(basePackages = {
        "com.cinema.dao",
        "com.cinema.service",
        "com.cinema.security"
})
public class AppConfig {
    private final Environment environment;

    public AppConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty("db.driver"));
        dataSource.setUrl(environment.getProperty("db.url"));
        dataSource.setUsername(environment.getProperty("db.username"));
        dataSource.setPassword(environment.getProperty("db.password"));
        return dataSource;
    }
    @Bean
    public LocalSessionFactoryBean getFactoryBean() {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setDataSource(getDataSource());
        Properties properties =new Properties();
        properties.put("hibernate.show.sql", environment.getProperty("hibernate.show.sql"));
        properties.put("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.hbm2ddl.auto"));
        factoryBean.setHibernateProperties(properties);
        factoryBean.setAnnotatedClasses(CinemaHall.class);
        factoryBean.setAnnotatedClasses(Movie.class);
        factoryBean.setAnnotatedClasses(MovieSession.class);
        factoryBean.setAnnotatedClasses(Order.class);
        factoryBean.setAnnotatedClasses(ShoppingCart.class);
        factoryBean.setAnnotatedClasses(Ticket.class);
        factoryBean.setAnnotatedClasses(User.class);
        return factoryBean;
    }
}
