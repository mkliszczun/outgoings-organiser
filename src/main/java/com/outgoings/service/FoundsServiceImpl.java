package com.outgoings.service;

import com.outgoings.entity.Account;
import com.outgoings.entity.Money;
import com.outgoings.exception.ResourceNotFoundException;
import com.outgoings.repository.MoneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoundsServiceImpl implements FoundsService {

    MoneyRepository moneyRepository;
    UserService userService;

    @Override
    public List<Money> getFounds(Account account) {

        Optional<List<Money>> founds = moneyRepository.findByAccount(account);
//        if (founds.isPresent()){
//            List<Money> founds1 = founds.get();
//            return founds1;
//        }

        return founds.orElse(null);
    }

    @Override
    public Money getValue(Account account, String value) {
        List<Money> founds = getFounds(account);

        for (Money money : founds){
            if (money.getCurrency().equalsIgnoreCase(value)){
                return money;
            }
        }
        return null;
    }

    @Override
    public List<Money> addMoney(Account account, Money money) {

        List<Money> founds = moneyRepository.findByAccount(account).get();
        boolean currencyFound = false;
        for (Money money1 : founds){
            if (money1.getCurrency().equalsIgnoreCase(money.getCurrency())){
                double amount = money1.getAmount();
                amount += money.getAmount();
                money1.setAmount(amount);
                currencyFound = true;
                break;
            }
        }
        if (!currencyFound){
            money.setAccount(account);
            founds.add(money);
        }

        userService.saveUser(account);

        return founds;
    }

    // removes all values from founds, except base value,
    @Override
    public void clearFounds(Account account) {
        List<Money> founds = getFounds(account);
        for (Money money : founds){
            if (!money.getCurrency().equalsIgnoreCase(account.getBaseValue())) founds.remove(money);
            else money.setAmount(0);
        }
    }

//    removes value from founds. If it's base currency its amount is set to 0
    @Override
    public void removeValue(Account account, String value) {
        for (Money money : account.getFounds()){
            if (!money.getCurrency().equalsIgnoreCase(value))
                account.getFounds().remove(money);
             else money.setAmount(0);
        }
    }

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
}
