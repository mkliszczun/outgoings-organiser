package com.outgoings.controller;

import com.outgoings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody String username, @RequestBody String password){
        String token = userService.login(username, password);

        if (token.isEmpty()) return "no token found for this username, and password";
        return token;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}