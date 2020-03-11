package com.outgoings.service;

import com.outgoings.entity.Account;
import com.outgoings.entity.Money;
import com.outgoings.repository.MoneyRepository;

import java.util.List;

public interface FoundsService {

    List<Money> getFounds(Account account);
    Money getCurrency(Account account, String currency);
    List<Money> addMoney(Account account, Money money);
    void clearFounds(Account account);
    void clearValue(Account account, String value);
    boolean containsCurrency(List<Money> founds, String currency);
}
