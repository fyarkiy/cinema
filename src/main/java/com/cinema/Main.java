package com.cinema;

import com.cinema.lib.Injector;
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

public class Main {
    private static Injector injector = Injector.getInstance("com.cinema");
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        Movie fastFurious = new Movie();
        fastFurious.setTitle("Fast and Furious");
        fastFurious.setDescritpion("action");
        MovieService movieService = (MovieService) injector.getInstance(MovieService.class);
        movieService.add(fastFurious);
        Movie bugs = new Movie();
        bugs.setTitle("Bugs");
        bugs.setDescritpion("Animation");
        movieService.add(bugs);
        movieService.getAll().forEach(System.out::println);
        CinemaHall redHall = new CinemaHall();
        redHall.setCapacity(100);
        redHall.setDescription("Red");
        CinemaHallService cinemaHallService = (CinemaHallService) injector
                .getInstance(CinemaHallService.class);
        cinemaHallService.add(redHall);

        CinemaHall blueHall = new CinemaHall();
        blueHall.setCapacity(120);
        blueHall.setDescription("Blue");
        cinemaHallService.add(blueHall);

        MovieSession morningSession = new MovieSession();
        morningSession.setCinemaHall(redHall);
        morningSession.setMovie(fastFurious);
        morningSession.setShowTime(LocalDateTime.of(2020, 10, 10, 10, 00, 00));
        MovieSessionService movieSessionService = (MovieSessionService) injector
                .getInstance(MovieSessionService.class);
        movieSessionService.add(morningSession);
        logger.info("Morning session " + morningSession + " added");

        MovieSession afternoonSession = new MovieSession();
        afternoonSession.setCinemaHall(blueHall);
        afternoonSession.setMovie(fastFurious);
        afternoonSession.setShowTime(LocalDateTime.of(2020, 10, 10, 16, 00, 00));
        movieSessionService.add(afternoonSession);
        logger.info("Afternoon session " + afternoonSession + " was added");

        MovieSession morningSessionAt13 = new MovieSession();
        morningSessionAt13.setCinemaHall(redHall);
        morningSessionAt13.setMovie(bugs);
        morningSessionAt13.setShowTime(LocalDateTime.of(2020, 10, 10, 13, 00, 00));
        movieSessionService.add(morningSessionAt13);
        logger.info("Morning session at 13 " + morningSessionAt13 + " was added");

        MovieSession tomorrowSession = new MovieSession();
        tomorrowSession.setCinemaHall(redHall);
        tomorrowSession.setMovie(fastFurious);
        tomorrowSession.setShowTime(LocalDateTime.of(2020, 10, 11, 21, 00, 00));
        movieSessionService.add(tomorrowSession);
        logger.info("tomorrow session " + tomorrowSession + " was added");

        movieService.getAll().forEach((ms) -> logger.info(ms.toString()));
        logger.info("get all cinema halls");
        cinemaHallService.getAll().forEach(logger :: info);
        movieSessionService.findAvailableSessions(1L, LocalDate.of(2020, 10, 10))
                .forEach((ms) -> logger.info(ms.toString()));

        AuthenticationService authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
        authenticationService.register("a@gmail.com", "abcd");
        authenticationService.register("ma@gmail.com", "dcba");
        UserService userService = (UserService) injector.getInstance(UserService.class);
        User userMa = userService.findByEmail("ma@gmail.com").get();
        authenticationService.register(userMa.getEmail(), userMa.getPassword());
        ShoppingCartService shoppingCartService =
                (ShoppingCartService) injector.getInstance((ShoppingCartService.class));
        shoppingCartService.addSession(morningSession, userMa);
        shoppingCartService.addSession(morningSession, userMa);

        User userA = userService.findByEmail("a@gmail.com").get();
        shoppingCartService.addSession(afternoonSession, userA);
        shoppingCartService.clear(shoppingCartService.getByUser(userA));
        logger.warn("Shopping cart for user " + userA + " was cleared");

        OrdersService ordersService = (OrdersService) injector.getInstance(OrdersService.class);
        Order order = ordersService.completeOrder(shoppingCartService
                .getByUser(userMa).getTickets(), userMa);
        shoppingCartService.addSession(afternoonSession, userMa);
        Order orderSecond = ordersService.completeOrder(shoppingCartService
                .getByUser(userMa).getTickets(), userMa);
        ordersService.getOrderHistory(userMa).forEach((o) -> logger.info(o.toString()));

        userService.findByEmail("maZ@gmail.com");
    }
}
