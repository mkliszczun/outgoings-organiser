package com.outgoings.service;

public interface ExchangeRateClient {
	double getRateAgainstPln(String currency) throws Exception;

	double checkCurrentRate(String fromCurrency, String toCurrency);
}
