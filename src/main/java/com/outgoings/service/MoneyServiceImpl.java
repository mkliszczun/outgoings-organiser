package com.outgoings.service;

import com.outgoings.entity.Account;
import com.outgoings.entity.Money;
import com.outgoings.repository.MoneyRepository;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoneyServiceImpl implements MoneyService {

  MoneyRepository moneyRepository;

  @Override
  @Transactional
  public List<Money> getMoney(Account account) {
    Optional<List<Money>> founds = moneyRepository.findByAccount(account);
    return founds.orElse(null);
  }

  @Override
  public Money getCurrency(Account account, String currency) {
    List<Money> founds = getMoney(account);

    if (founds == null) return null;
    for (Money money : founds) {
      if (money.getCurrency().equalsIgnoreCase(currency)) {
        return money;
      }
    }
    return null;
  }

  @Override
  @Transactional
  public Money addMoney(Account account, Money money) {

    Money desiredCurrency = getCurrency(account, money.getCurrency());
    if (desiredCurrency == null) {
      money.setAccount(account);
      money.setId(0); // to prevent from unwanted update
      moneyRepository.save(money);
      return money;
    } else {
      double sum = desiredCurrency.getAmount() + money.getAmount();
      desiredCurrency.setAmount(sum);
      return desiredCurrency;
    }
  }

  @Override
  @Transactional
  public void clearFounds(Account account) {
    List<Money> founds = getMoney(account);
    for (Money money : founds) {
      moneyRepository.delete(money);
    }
  }

  @Override
  @Transactional
  public void deleteValue(Account account, String currency) {
    Money money = getCurrency(account, currency);
    moneyRepository.delete(money);
  }

  //    checks if given currency is present in given founds
  @Override
  public boolean containsCurrency(List<Money> founds, String currency) {
    for (Money money : founds) if (money.getCurrency().equalsIgnoreCase(currency)) return true;
    return false;
  }

  @Autowired
  public void setMoneyRepository(MoneyRepository moneyRepository) {
    this.moneyRepository = moneyRepository;
  }
}
