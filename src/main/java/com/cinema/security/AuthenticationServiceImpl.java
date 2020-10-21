package com.cinema.security;

import com.cinema.exception.AuthenticationException;
import com.cinema.model.User;
import com.cinema.service.ShoppingCartService;
import com.cinema.service.UserService;
import com.cinema.util.HashUtil;
import org.springframework.stereotype.Repository;

@Repository
public class AuthenticationServiceImpl implements AuthenticationService {
    private UserService userService;
    private ShoppingCartService shoppingCartService;

    public AuthenticationServiceImpl(UserService userService, ShoppingCartService shoppingCartService) {
        this.userService = userService;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public User login(String email, String password) throws AuthenticationException {
        return userService.findByEmail(email)
                .filter(u -> HashUtil.hashPassword(password, u.getSalt()).equals(u.getPassword()))
                .orElseThrow(() -> new AuthenticationException("Incorrect email or password"));
    }

    @Override
    public User register(String email, String password) {
        User user = new User();
        user.setPassword(password);
        user.setEmail(email);
        userService.add(user);
        shoppingCartService.registerNewShoppingCart(user);
        return user;
    }
}
