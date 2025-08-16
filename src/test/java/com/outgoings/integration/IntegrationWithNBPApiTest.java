package com.outgoings.integration;

import com.outgoings.infrastructure.ExchangeRateClientImpl;
import com.outgoings.service.ExchangeRateClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Todo - refactor for smokeTest
@Disabled
public class IntegrationWithNBPApiTest {

	HttpClient httpClient = HttpClient.newHttpClient();
	ExchangeRateClient rateClient = new ExchangeRateClientImpl(httpClient);



	@Test
	void testConnectionWithNBP() throws Exception{
		String table = "a";
		String currency = "EUR";

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

		assertEquals(200, response.statusCode());
	}

	@Test
	void call_shouldReturnPositiveRate() throws Exception {
		double rate = rateClient.getRateAgainstPln("EUR");
		assertTrue(0 < rate);
	}

}
