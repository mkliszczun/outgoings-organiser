package com.outgoings.controller;

import com.outgoings.entity.Account;
import com.outgoings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  UserService userService;

  @PostMapping("/login")
  public String login(@RequestBody Account account) {

    String token = userService.login(account.getUsername(), account.getPassword());

    if (token.isEmpty()) return "no token found for this username, and password";
    return token;
  }

  @PostMapping("/api/logout")
  public String logout(Authentication authentication) {
    Account account = userService.findByUsername(authentication.getName());
    account.setToken("");
    return "Successfully logged out";
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }
}
