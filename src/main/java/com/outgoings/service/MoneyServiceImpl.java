package com.outgoings.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outgoings.entity.Account;
import com.outgoings.entity.Money;
import com.outgoings.infrastructure.ExchangeRateClientImpl;
import com.outgoings.repository.MoneyRepository;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoneyServiceImpl implements MoneyService {

  MoneyRepository moneyRepository;

  private final HttpClient httpClient;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private final ExchangeRateClient rateClient;

	public MoneyServiceImpl(HttpClient httpClient, MoneyRepository moneyRepository, ExchangeRateClient exchangeRateClient) {
		this.httpClient = httpClient;
		this.moneyRepository = moneyRepository;
		this.rateClient = exchangeRateClient;

	}

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

  @SuppressWarnings({"checkstyle:WhitespaceAround", "CheckStyle"})
  @Override
  public double calculateValue(Account account, String currency){
	  List<Money> moneyList = getMoney(account);
	  if (moneyList == null){
		  return -99999; // a message suggesting that moneyList is null
	  }
	  double totalValue = 0;
	  for (Money money:
		   moneyList) {
		  if (money.getCurrency() != currency) {
			  String moneyCurrency = money.getCurrency();
			  double currentRate = rateClient.checkCurrentRate(moneyCurrency, currency);
			  double amount = money.getAmount();
			  totalValue += (amount * currentRate);
		  } else {
			  totalValue += money.getAmount();
		  }
	  }
	  // loop for each money in the list
      return totalValue;
  }
//TODO - test me

//  @SuppressWarnings({"checkstyle:WhitespaceAround", "CheckStyle"})
//  public double checkCurrentRate(String fromCurrency, String toCurrency) {
//	  try {
//		  // identical currencies → 1
//		  if (fromCurrency.equalsIgnoreCase(toCurrency)) return 1.0;
//
//		  // base value PLN
//		  if ("PLN".equalsIgnoreCase(toCurrency)) {
//			  return getRateAgainstPln(fromCurrency);
//		  }
//		  if ("PLN".equalsIgnoreCase(fromCurrency)) {
//			  return 1.0 / getRateAgainstPln(toCurrency);
//		  }
//
//		  // cross rate - NBP Api returns rate only against PLN
//		  // if toCurrency is anything else this needs to be done
//		  double fromPln = getRateAgainstPln(fromCurrency);
//		  double toPln   = getRateAgainstPln(toCurrency);
//		  return fromPln / toPln;
//
//	  } catch (Exception e) {
//		  e.printStackTrace();
//		  return -1;
//	  }
//  }
//  /**
//	 * Downloads rate from Table A or if not available from table B
//	 *
//	 * @param currency code ISO 4217
//	 * @return rate (double)
//	 */
//	@SuppressWarnings({"checkstyle:WhitespaceAround", "CheckStyle"})
//
//	private double getRateAgainstPln(String currency)
//		throws Exception {
//
//		if ("PLN".equalsIgnoreCase(currency)) return 1.0;
//
//		for (String table : Arrays.asList("a", "b", "c")) {
//			String url = String.format(
//				"https://api.nbp.pl/api/exchangerates/rates/%s/%s/?format=json",
//				table, currency.toLowerCase());
//
//			HttpRequest request = HttpRequest.newBuilder()
//				.uri(URI.create(url))
//				.header("Accept", "application/json")
//				.GET()
//				.build();
//
//			HttpResponse<String> response =
//				httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//			if (response.statusCode() == 200) {
//				JsonNode root = objectMapper.readTree(response.body());
//				return root.path("rates").get(0).path("bid").asDouble();
//			}
//			if (response.statusCode() != 404) {
//				throw new RuntimeException(
//					"NBP error " + response.statusCode() + " for " + currency);
//			}
//			// 404 → tries again with another table
//		}
//		throw new IllegalArgumentException(
//			"Currency " + currency + " not found in NBP tables A or B");
//	}


  @Autowired
  public void setMoneyRepository(MoneyRepository moneyRepository) {
    this.moneyRepository = moneyRepository;
  }
}
