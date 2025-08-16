package com.outgoings.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outgoings.entity.Account;
import com.outgoings.entity.Money;
import com.outgoings.repository.MoneyRepository;
import java.net.http.HttpClient;
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

  @Mock private ExchangeRateClient rateClient;

  @Mock private HttpClient httpClient;
  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void addMoney_newCurrencyShouldSaveMoney() {
    Account account = new Account();
    account.setId(1);

    Money existing = new Money();
    existing.setCurrency("PLN");
    existing.setAmount(100);

    Mockito.when(moneyRepository.findByAccount(account))
        .thenReturn(Optional.of(Collections.singletonList(existing)));
    Mockito.when(moneyRepository.save(any(Money.class)))
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
    Mockito.verify(moneyRepository, Mockito.never()).save(any());
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

    Mockito.verify(moneyRepository, Mockito.times(2)).delete(any(Money.class));
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

  //TODO - separate test
//  @Test
//	void checkCurrentRate_shouldReturn1WhenIdentical() {
//	  double result = moneyService.checkCurrentRate("EUR", "EUR");
//	  assertEquals(result, 1.0);
//  }
// Todo - separate test
//  @Test
//	void checkCurrentRate_shouldReturnResultFromApiAsDoubleWhenRequestedToPLN() throws Exception {
//
//	  String jsonMock = """
//        {
//          "table": "C",
//          "currency": "dolar amerykański",
//          "code": "USD",
//          "rates": [
//            {
//              "no": "149/c/NBP/2025",
//              "effectiveDate": "2025-08-08",
//              "bid": 3.6202,
//              "ask": 3.6934
//            }
//          ]
//        }
//        """;
//
//	  @SuppressWarnings("unchecked")
//	  HttpResponse<String> mockResponse = (HttpResponse<String>) mock(HttpResponse.class);
//	  Mockito.when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).
//		  thenReturn(mockResponse);
//
//	  Mockito.when(mockResponse.statusCode()).thenReturn(200);
//	  Mockito.when(mockResponse.body()).thenReturn(jsonMock);
//
//	  double rate = moneyService.checkCurrentRate("USD", "PLN");
//      Mockito.verify(mockResponse, times(1)).body();
//	  assertEquals(3.6202, rate);
//
//  }
// todo - separate test
//  @Test
//	void checkCurrentRate_shouldDoCrossCalculationWhenCheckingToNotPLNValues() throws Exception {
//	  String jsonMock1 = """
//        {
//          "table": "C",
//          "currency": "dolar amerykański",
//          "code": "USD",
//          "rates": [
//            {
//              "no": "149/c/NBP/2025",
//              "effectiveDate": "2025-08-08",
//              "bid": 3.6202,
//              "ask": 3.6934
//            }
//          ]
//        }
//        """;
//	  String jsonMock2 = """
//        {
//          "table": "C",
//          "currency": "euro",
//          "code": "EUR",
//          "rates": [
//            {
//              "no": "149/c/NBP/2025",
//              "effectiveDate": "2025-08-08",
//              "bid": 4.1205,
//              "ask": 4.2029
//            }
//          ]
//        }
//        """;
//
//	  @SuppressWarnings("unchecked")
//	  HttpResponse<String> res1 = (HttpResponse<String>) mock(HttpResponse.class);
//	  @SuppressWarnings("unchecked")
//	  HttpResponse<String> res2 = (HttpResponse<String>) mock(HttpResponse.class);
//
//	  Mockito.when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
//		  .thenReturn(res1, res2);
//	  when(res1.statusCode()).thenReturn(200);
//	  when(res1.body()).thenReturn(jsonMock1);
//	  when(res2.statusCode()).thenReturn(200);
//	  when(res2.body()).thenReturn(jsonMock2);
//
//	  double rate = moneyService.checkCurrentRate("USD", "EUR");
//	  assertEquals(0.878582696274724, rate);
//  }

  @Test
	void calculateValue_shouldReturnCorrectTotalValueOfDifferentRates(){

	  Money money1 = new Money();
	  money1.setCurrency("USD");
	  money1.setAmount(10);
	  Money money2 = new Money();
	  money2.setCurrency("EUR");
	  money2.setAmount(10);
	  Money money3 = new Money();
	  money3.setCurrency("PLN");
	  money3.setAmount(5);

	  List<Money> moneyList = new ArrayList<>();
	  moneyList.add(money1);
	  moneyList.add(money2);
	  moneyList.add(money3);
	  when(moneyRepository.findByAccount(any(Account.class))).thenReturn(Optional.of(moneyList));
	  when(rateClient.checkCurrentRate("USD", "PLN")).thenReturn(3.9);
	  when(rateClient.checkCurrentRate("EUR","PLN")).thenReturn(4.3);

	  double calculatedValue = moneyService.calculateValue(new Account(), "PLN");

	  assertEquals(87, calculatedValue);
  }
}
