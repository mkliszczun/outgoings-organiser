package com.outgoings.service;

import com.outgoings.entity.Account;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public interface UserService {
    String login(String username, String password);
    Optional<User> findByToken(String token);
    Account findByUsername(String nickname);
    Account findById(int id);
    boolean checkifFree(String username);
    Account saveUser(Account user);
}
