package com.cinema;

import com.cinema.config.AppConfig;
import com.cinema.model.CinemaHall;
import com.cinema.model.Movie;
import com.cinema.model.MovieSession;
import com.cinema.model.Order;
import com.cinema.model.User;
import com.cinema.security.AuthenticationService;
import com.cinema.service.CinemaHallService;
import com.cinema.service.MovieService;
import com.cinema.service.MovieSessionService;
import com.cinema.service.OrdersService;
import com.cinema.service.ShoppingCartService;
import com.cinema.service.UserService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);
        MovieService movieService = context.getBean(MovieService.class);
        Movie fastFurious = new Movie();
        fastFurious.setTitle("Fast and Furious");
        fastFurious.setDescritpion("action");
        movieService.add(fastFurious);
        Movie bugs = new Movie();


        bugs.setTitle("Bugs");
        bugs.setDescritpion("Animation");
        movieService.add(bugs);
        movieService.getAll().forEach(System.out::println);
        CinemaHall redHall = new CinemaHall();
        redHall.setCapacity(100);
        redHall.setDescription("Red");
        CinemaHallService cinemaHallService = context.getBean(CinemaHallService.class);
        cinemaHallService.add(redHall);

        CinemaHall blueHall = new CinemaHall();
        blueHall.setCapacity(120);
        blueHall.setDescription("Blue");
        cinemaHallService.add(blueHall);

        MovieSession morningSession = new MovieSession();
        morningSession.setCinemaHall(redHall);
        morningSession.setMovie(fastFurious);
        morningSession.setShowTime(LocalDateTime.of(2020, 10, 10, 10, 00, 00));
        MovieSessionService movieSessionService = context.getBean(MovieSessionService.class);
        movieSessionService.add(morningSession);

        MovieSession afternoonSession = new MovieSession();
        afternoonSession.setCinemaHall(blueHall);
        afternoonSession.setMovie(fastFurious);
        afternoonSession.setShowTime(LocalDateTime.of(2020, 10, 10, 16, 00, 00));
        movieSessionService.add(afternoonSession);

        MovieSession morningSessionAt13 = new MovieSession();
        morningSessionAt13.setCinemaHall(redHall);
        morningSessionAt13.setMovie(bugs);
        morningSessionAt13.setShowTime(LocalDateTime.of(2020, 10, 10, 13, 00, 00));
        movieSessionService.add(morningSessionAt13);

        MovieSession tomorrowSession = new MovieSession();
        tomorrowSession.setCinemaHall(redHall);
        tomorrowSession.setMovie(fastFurious);
        tomorrowSession.setShowTime(LocalDateTime.of(2020, 10, 11, 21, 00, 00));
        movieSessionService.add(tomorrowSession);

        movieService.getAll().forEach(logger::info);
        logger.info("get all cinema halls");
        cinemaHallService.getAll().forEach(logger::info);
        movieSessionService.findAvailableSessions(1L, LocalDate.of(2020, 10, 10))
                .forEach(logger::info);

        AuthenticationService authenticationService =
                context.getBean(AuthenticationService.class);
        authenticationService.register("a@gmail.com", "abcd");
        authenticationService.register("ma@gmail.com", "dcba");
        UserService userService = context.getBean(UserService.class);
        User userMa = userService.findByEmail("ma@gmail.com").get();
        ShoppingCartService shoppingCartService =
                context.getBean(ShoppingCartService.class);
        shoppingCartService.addSession(morningSession, userMa);
        shoppingCartService.addSession(morningSession, userMa);

        User userA = userService.findByEmail("a@gmail.com").get();
        shoppingCartService.addSession(afternoonSession, userA);
        shoppingCartService.clear(shoppingCartService.getByUser(userA));
        logger.warn("Shopping cart for user " + userA + " was cleared");

        OrdersService ordersService = context.getBean(OrdersService.class);
        Order order = ordersService.completeOrder(shoppingCartService
                .getByUser(userMa).getTickets(), userMa);
        shoppingCartService.addSession(afternoonSession, userMa);
        Order orderSecond = ordersService.completeOrder(shoppingCartService
                .getByUser(userMa).getTickets(), userMa);
        ordersService.getOrderHistory(userMa).forEach(System.out::println);
        ordersService.getOrderHistory(userMa).forEach(logger::info);

        userService.findByEmail("maZ@gmail.com");
    }
}
