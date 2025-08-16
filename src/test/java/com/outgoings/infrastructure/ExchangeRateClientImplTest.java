package com.outgoings.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateClientImplTest {
	@Mock
	private HttpClient httpClient;

	private ObjectMapper objectMapper = new ObjectMapper();

	@InjectMocks
	private ExchangeRateClientImpl exchangeRateClient;

	@Test
	void getRateAgainstPln_shouldSendRequestToApi() throws Exception{
		//when invoked methot should send request to Api there should be one request and currency should be passed
		// with request
		HttpResponse response = mock(HttpResponse.class);
	    String jsonMock = """
          {
            "table": "C",
            "currency": "euro",
            "code": "EUR",
            "rates": [
              {
                "no": "149/c/NBP/2025",
                "effectiveDate": "2025-08-08",
                "mid": 4.1205
              }
            ]
          }
          """;
		when(httpClient.send(any(), any())).thenReturn(response);
		when(response.statusCode()).thenReturn(200);
		when(response.body()).thenReturn(jsonMock);

		String expectedPartOfUrl = "eur";

		exchangeRateClient.getRateAgainstPln("EUR");

		ArgumentCaptor<HttpRequest> captor = ArgumentCaptor.forClass(HttpRequest.class);
		verify(httpClient, times(1)).send(captor.capture(), any(HttpResponse.BodyHandler.class));

		HttpRequest captured = captor.getValue();

		assertTrue(captured.uri().toString().toLowerCase().contains(expectedPartOfUrl));
	}


}
