package com.outgoings.service;

import static org.junit.jupiter.api.Assertions.*;

import com.outgoings.entity.Account;
import com.outgoings.entity.Money;
import com.outgoings.repository.MoneyRepository;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MoneyServiceImplTest {

  @Mock private MoneyRepository moneyRepository;

  @InjectMocks private MoneyServiceImpl moneyService;

  @Test
  void addMoney_newCurrencyShouldSaveMoney() {
    Account account = new Account();
    account.setId(1);

    Money existing = new Money();
    existing.setCurrency("PLN");
    existing.setAmount(100);

    Mockito.when(moneyRepository.findByAccount(account))
        .thenReturn(Optional.of(Collections.singletonList(existing)));
    Mockito.when(moneyRepository.save(Mockito.any(Money.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Money money = new Money();
    money.setId(5);
    money.setCurrency("USD");
    money.setAmount(50);

    Money result = moneyService.addMoney(account, money);

    assertSame(money, result);
    assertEquals(account, money.getAccount());
    assertEquals(0, money.getId());
    Mockito.verify(moneyRepository).save(money);
  }

  @Test
  void addMoney_existingCurrencyShouldUpdateAmount() {
    Account account = new Account();
    account.setId(1);

    Money existing = new Money();
    existing.setCurrency("USD");
    existing.setAmount(40);

    Mockito.when(moneyRepository.findByAccount(account))
        .thenReturn(Optional.of(Collections.singletonList(existing)));

    Money addition = new Money();
    addition.setCurrency("USD");
    addition.setAmount(60);

    Money result = moneyService.addMoney(account, addition);

    assertSame(existing, result);
    assertEquals(100, existing.getAmount());
    Mockito.verify(moneyRepository, Mockito.never()).save(Mockito.any());
  }

  @Test
  void getCurrency_shouldReturnNullWhenNotFound() {
    Account account = new Account();
    Mockito.when(moneyRepository.findByAccount(account))
        .thenReturn(Optional.of(Collections.emptyList()));

    Money result = moneyService.getCurrency(account, "EUR");

    assertNull(result);
  }

  @Test
  void clearFounds_shouldDeleteAllMoney() {
    Account account = new Account();
    Money m1 = new Money();
    Money m2 = new Money();
    List<Money> list = new ArrayList<>(Arrays.asList(m1, m2));
    Mockito.when(moneyRepository.findByAccount(account)).thenReturn(Optional.of(list));

    moneyService.clearFounds(account);

    Mockito.verify(moneyRepository, Mockito.times(2)).delete(Mockito.any(Money.class));
  }

  @Test
  void getMoney_shouldReturnMoneyList() {
    Account account = new Account();
    List<Money> list = Arrays.asList(new Money(), new Money());
    Mockito.when(moneyRepository.findByAccount(account)).thenReturn(Optional.of(list));

    List<Money> result = moneyService.getMoney(account);

    assertSame(list, result);
  }

  @Test
  void deleteValue_shouldRemoveGivenCurrency() {
    Account account = new Account();
    Money money = new Money();
    money.setCurrency("EUR");
    Mockito.when(moneyRepository.findByAccount(account))
        .thenReturn(Optional.of(Collections.singletonList(money)));

    moneyService.deleteValue(account, "EUR");

    Mockito.verify(moneyRepository).delete(money);
  }

  @Test
  void containsCurrency_shouldCheckCurrencyPresence() {
    Money usd = new Money();
    usd.setCurrency("USD");
    List<Money> list = Collections.singletonList(usd);

    assertTrue(moneyService.containsCurrency(list, "usd"));
    assertFalse(moneyService.containsCurrency(list, "eur"));
  }
}
