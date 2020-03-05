package com.outgoings.service;

import com.outgoings.entity.Account;
import com.outgoings.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {

    UserRepository userRepository;



    @Override
    public String login(String username, String password) {
        Optional<Account> userTemp = userRepository.login(username,password);
        if(userTemp.isPresent()){
            String token = UUID.randomUUID().toString();
            Account user = userTemp.get();
            user.setToken(token);
            userRepository.save(user);
            return token;
        }

        return "";
    }

    @Override
    public Optional<User> findByToken(String token) {
        Optional<Account> user = userRepository.findByToken(token);
        if (user.isPresent()){
            Account user1 = user.get();
            User user2 = new User(user1.getUsername(), user1.getPassword(),
                    true, true,
                    true, true, AuthorityUtils.createAuthorityList("USER"));
            return Optional.of(user2);
        }
        return Optional.empty();
    }

    @Override
    public Account findByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    @Override
    public Account findById(int id) {
        Optional<Account> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public boolean checkifFree(String username) {
        if (userRepository.findByUsername(username).isPresent()) return false;

        return true;

    }

    @Override
    public Account saveUser(Account user) {
        return userRepository.save(user);
    }


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
