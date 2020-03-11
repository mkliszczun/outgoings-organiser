package com.outgoings.service;

import com.outgoings.entity.Account;
import com.outgoings.entity.Money;
import com.outgoings.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {

    AccountRepository accountRepository;

    @Override
    public String login(String username, String password) {
        Optional<Account> userTemp = accountRepository.login(username,password);
        if(userTemp.isPresent()){
            String token = UUID.randomUUID().toString();
            Account user = userTemp.get();
            user.setToken(token);
            accountRepository.save(user);
            return token;
        }
        return "";
    }

    @Override
    public Optional<User> findByToken(String token) {
        Optional<Account> user = accountRepository.findByToken(token);
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
        return accountRepository.findByUsername(username).get();
    }

    @Override
    public Account findById(int id) {
        Optional<Account> user = accountRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public boolean checkIfFree(String username) {
        return !accountRepository.findByUsername(username).isPresent();
    }

    @Override
    public Account saveUser(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account addAccount(Account account) {
        Money money = new Money();
        money.setCurrency(account.getBaseValue());
        money.setAmount(0);
        money.setAccount(account);
        List<Money> founds = new ArrayList<>();
        founds.add(money);
        account.setFounds(founds);
        return accountRepository.save(account);
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}