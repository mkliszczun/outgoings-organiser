package com.outgoings.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class HttpClientConfig {
	@SuppressWarnings("checkstyle:FileTabCharacter")
	@Bean
	public HttpClient httpClient() {
		return HttpClient.newBuilder()
			.version(HttpClient.Version.HTTP_2)
			.followRedirects(HttpClient.Redirect.NORMAL)
			.connectTimeout(Duration.ofSeconds(5))
			.build();
	}
}
