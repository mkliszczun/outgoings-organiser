package com.outgoings.service;

import com.outgoings.entity.Account;
import java.util.Optional;
import org.springframework.security.core.userdetails.User;

public interface UserService {
  String login(String username, String password);

  Optional<User> findByToken(String token);

  Account findByUsername(String nickname);

  Account findById(int id);

  boolean checkIfFree(String username);

  Account saveUser(Account user);
}
