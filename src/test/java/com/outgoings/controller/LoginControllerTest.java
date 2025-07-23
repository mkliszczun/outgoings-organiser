package com.outgoings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outgoings.entity.Account;
import com.outgoings.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(
	controllers = LoginController.class,
	excludeFilters = @ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = com.outgoings.security.SecurityConfiguration.class
	)
)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)

class LoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void loginShouldReturnTokenWhenCredentialsValid() throws Exception {
		Account acc = new Account();
		acc.setUsername("john");
		acc.setPassword("pass");

		Mockito.when(userService.login("john", "pass")).thenReturn("abc123");

		mockMvc.perform(post("/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(acc)))
			.andExpect(status().isOk())
			.andExpect(content().string("abc123"));
	}

	@Test
	void loginShouldReturnMessageWhenCredentialsInvalid() throws Exception {
		Account acc = new Account();
		acc.setUsername("john");
		acc.setPassword("wrong");

		Mockito.when(userService.login("john", "wrong")).thenReturn("");

		mockMvc.perform(post("/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(acc)))
			.andExpect(status().isOk())
			.andExpect(content().string("no token found for this username, and password"));
	}
}
