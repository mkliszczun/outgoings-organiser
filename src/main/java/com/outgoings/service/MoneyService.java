package com.outgoings.service;

import com.outgoings.entity.Account;
import com.outgoings.entity.Money;
import com.outgoings.repository.MoneyRepository;

import java.util.List;

public interface MoneyService {

    List<Money> getMoney(Account account);
    Money getCurrency(Account account, String currency);
    Money addMoney(Account account, Money money);
    void clearFounds(Account account);
    void deleteValue(Account account, String value);
    boolean containsCurrency(List<Money> founds, String currency);
}
