package com.outgoings.service;

import com.outgoings.entity.Account;
import com.outgoings.entity.Money;
import com.outgoings.repository.MoneyRepository;
import com.outgoings.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class FoundsServiceImpl implements FoundsService {

    MoneyRepository moneyRepository;
    UserService userService;
    AccountRepository accountRepository;

    @Override
    @Transactional
    public List<Money> getFounds(Account account) {
        Optional<List<Money>> founds = moneyRepository.findByAccount(account);
        return founds.orElse(null);
    }

    @Override
    public Money getCurrency(Account account, String currency) {
        List<Money> founds = getFounds(account);

        for (Money money : founds){
            if (money.getCurrency().equalsIgnoreCase(currency)){
                return money;
            }
        }
        return null;
    }

    @Override
    public List<Money> addMoney(Account account, Money money) {
        List<Money> founds;
//        Optional<List<Money>> foundsOpt = moneyRepository.findByAccount(account);
        founds = getFounds(account);
        boolean currencyFound = false;
        for (Money money1 : founds){
            if (money1.getCurrency().equalsIgnoreCase(money.getCurrency())){
                double amount = money1.getAmount() + money.getAmount();
                money1.setAmount(amount);
                currencyFound = true;
                break;
            }
        }
        if (!currencyFound){
            founds.add(money);
            money.setAccount(account);
            moneyRepository.save(money);
        }

        userService.saveUser(account);

        return founds;
    }

    // removes all values from founds, except base value,
    @Override
    public void clearFounds(Account account) {
        List<Money> founds = getFounds(account);
        for (Money money : founds){
            moneyRepository.delete(money);
        }

        founds.clear();

        Money money = new Money();
        money.setAmount(0);
        money.setCurrency(account.getBaseValue());
        founds.add(money);
        money.setAccount(account);
        moneyRepository.save(money);

        userService.saveUser(account);

    }

//    sets given value count to 0
    @Override
    public void clearValue(Account account, String currency) {
        Money money = getCurrency(account, currency);
        money.setAmount(0);
        moneyRepository.save(money);
    }

//    checks if given currency is present in given founds
    @Override
    public boolean containsCurrency(List<Money> founds, String currency) {
        for (Money money : founds)
            if(money.getCurrency().equalsIgnoreCase(currency)) return true;
        return false;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMoneyRepository(MoneyRepository moneyRepository) {
        this.moneyRepository = moneyRepository;
    }

    @Autowired
    public void setAccountRepository(AccountRepository repository) {
        this.accountRepository = repository;
    }
}
