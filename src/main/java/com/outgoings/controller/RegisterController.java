package com.outgoings.controller;

import com.outgoings.entity.Account;
import com.outgoings.exception.TakenUsernameException;
import com.outgoings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegisterController {

    UserService userService;

    @PostMapping("/register")
    public Account createNewAccount(@RequestBody @Valid Account user) {
        if (!userService.checkifFree(user.getUsername())) {
            throw new TakenUsernameException("This username is already taken");
        }
        user.setId(0);

        user.setAuthorities(null);

        return userService.saveUser(user);
    }

    @GetMapping
    public Account getEmptyAccount(){
        return new Account();
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}