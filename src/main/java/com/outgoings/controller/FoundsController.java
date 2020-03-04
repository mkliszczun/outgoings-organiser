package com.outgoings.controller;

import com.outgoings.entity.Money;
import com.outgoings.repository.MoneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/founds")
public class FoundsController {

    MoneyRepository moneyRepository;

    @GetMapping
    List<Money> getFounds(Authentication authentication){
        String token = authentication.getName();
        List<Money> founds = new ArrayList<>();
        return founds;
    }

    @Autowired
    public void setMoneyRepository(MoneyRepository moneyRepository) {
        this.moneyRepository = moneyRepository;
    }
}
