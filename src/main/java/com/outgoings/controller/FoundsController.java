package com.outgoings.controller;

import com.outgoings.entity.Account;
import com.outgoings.entity.Money;
import com.outgoings.exception.ResourceNotFoundException;
import com.outgoings.repository.MoneyRepository;
import com.outgoings.repository.UserRepository;
import com.outgoings.service.FoundsService;
import com.outgoings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/founds")
public class FoundsController {

    FoundsService foundsService;
    UserService userService;

    @GetMapping
    List<Money> getFounds(Authentication authentication){
        String username = authentication.getName();

        Account account = userService.findByUsername(username);

        List<Money> founds = foundsService.getFounds(account);

        return founds;
    }

    @PostMapping
    List<Money> setFounds(Authentication authentication, List<Money> founds){
        Account account = userService.findByUsername(authentication.getName());
        if (!foundsService.containsCurrency(founds, account.getBaseValue())) {
            Money money = new Money();
            money.setAmount(0);
            money.setCurrency(account.getBaseValue());
            founds.add(money);
        }
        account.setFounds(founds);
        userService.saveUser(account);

        return founds;
    }

    @PutMapping
    List<Money> addFounds(@RequestBody Money money, Authentication authentication){
        Account account = userService.findByUsername(authentication.getName());

        return foundsService.addMoney(account, money);
    }

    @DeleteMapping
    String clearFounds(Authentication authentication){
        Account account = userService.findByUsername(authentication.getName());
        foundsService.clearFounds(account);
        return "Founds Cleared";
    }

    @DeleteMapping("/{value}")
    String removeValue(Authentication authentication, @PathVariable String value){
        Account account = userService.findByUsername(authentication.getName());
        foundsService.removeValue(account, value);

        return "value %s removed" + value;
    }

    @GetMapping("/{value}")
    Money getParticularValue(@PathVariable String money, Authentication authentication){
        Account account = userService.findByUsername(authentication.getName());
        Money money1 = foundsService.getValue(account, money);
        if (money1 != null)return money1;
        throw new ResourceNotFoundException("Money not found");
    }

    @Autowired
    public void setFoundsService(FoundsService foundsService) {
        this.foundsService = foundsService;
    }

    @Autowired
    public void setUserService(UserService userRepository) {
        this.userService = userRepository;
    }
}
