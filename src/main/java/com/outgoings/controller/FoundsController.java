package com.outgoings.controller;

import com.outgoings.entity.Account;
import com.outgoings.entity.Money;
import com.outgoings.exception.ResourceNotFoundException;
import com.outgoings.service.MoneyService;
import com.outgoings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/founds")
public class FoundsController {

    MoneyService moneyService;
    UserService userService;

    @GetMapping
    List<Money> getFounds(Authentication authentication){
        String username = authentication.getName();

        Account account = userService.findByUsername(username);

        List<Money> founds = moneyService.getMoney(account);

        return founds;
    }

    @PostMapping
    List<Money> setFounds(Authentication authentication, List<Money> founds){
        Account account = userService.findByUsername(authentication.getName());
        if (!moneyService.containsCurrency(founds, account.getBaseValue())) {
            Money money = new Money();
            money.setAmount(0);
            money.setCurrency(account.getBaseValue());
            founds.add(money);
        }

        return founds;
    }

    @PutMapping
    Money addFounds(@RequestBody Money money, Authentication authentication){
        Account account = userService.findByUsername(authentication.getName());

        return moneyService.addMoney(account, money);
    }

    @DeleteMapping
    String clearFounds(Authentication authentication){
        Account account = userService.findByUsername(authentication.getName());
        moneyService.clearFounds(account);
        return "Founds Cleared";
    }

    @DeleteMapping("/{value}")
    String removeValue(Authentication authentication, @PathVariable String value){
        Account account = userService.findByUsername(authentication.getName());
        moneyService.deleteValue(account, value);

        return "value "+ value + " removed";
    }

    @GetMapping("/{value}")
    Money getParticularValue(@PathVariable String value, Authentication authentication){
        Account account = userService.findByUsername(authentication.getName());
        Money money1 = moneyService.getCurrency(account, value);
        if (money1 != null)return money1;
        throw new ResourceNotFoundException("Money not found");
    }

    @Autowired
    public void setMoneyService(MoneyService moneyService) {
        this.moneyService = moneyService;
    }

    @Autowired
    public void setUserService(UserService userRepository) {
        this.userService = userRepository;
    }
}