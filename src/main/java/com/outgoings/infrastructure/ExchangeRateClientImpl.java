package com.outgoings.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outgoings.service.ExchangeRateClient;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

@SuppressWarnings("checkstyle:FileTabCharacter")
@Service
public class ExchangeRateClientImpl implements ExchangeRateClient {

	private final HttpClient httpClient;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public ExchangeRateClientImpl(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	//Todo - fix "bid" "mid" in tables (a and b has mid)
	@SuppressWarnings({"checkstyle:FileTabCharacter", "checkstyle:Indentation"})
	@Override
	public double getRateAgainstPln(String currency) throws Exception {

		if ("PLN".equalsIgnoreCase(currency)) return 1.0;

		for (String table : Arrays.asList("a", "b", "c")) {
			String url = String.format(
				"https://api.nbp.pl/api/exchangerates/rates/%s/%s/?format=json",
				table, currency.toLowerCase());

			HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("Accept", "application/json")
				.GET()
				.build();

			HttpResponse<String> response =
				httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {
				JsonNode root = objectMapper.readTree(response.body());
				return root.path("rates").get(0).path("mid").asDouble();
			}
			if (response.statusCode() != 404) {
				throw new RuntimeException(
					"NBP error " + response.statusCode() + " for " + currency);
			}
			// 404 → tries again with another table
		}
		throw new IllegalArgumentException(
			"Currency " + currency + " not found in NBP tables A or B");
	}

	/**
	 *
	 * @param fromCurrency code ISO 4217
	 * @param toCurrency code ISO 4217
	 * @return double - rate of fromCurrency to toCurrency
	 *
	 */
	@SuppressWarnings({"checkstyle:FileTabCharacter", "checkstyle:Indentation"})
	@Override
	public double checkCurrentRate(String fromCurrency, String toCurrency) {
		try {
			// identical currencies → 1
			if (fromCurrency.equalsIgnoreCase(toCurrency)) return 1.0;

			// base value PLN
			if ("PLN".equalsIgnoreCase(toCurrency)) {
				return getRateAgainstPln(fromCurrency);
			}
			if ("PLN".equalsIgnoreCase(fromCurrency)) {
				return 1.0 / getRateAgainstPln(toCurrency);
			}

			// cross rate - NBP Api returns rate only against PLN
			// if toCurrency is anything else this needs to be done
			double fromPln = getRateAgainstPln(fromCurrency);
			double toPln   = getRateAgainstPln(toCurrency);
			return fromPln / toPln;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

}
