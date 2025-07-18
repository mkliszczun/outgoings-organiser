package com.outgoings.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outgoings.entity.Account;
import com.outgoings.entity.Transaction;
import com.outgoings.service.TransactionService;
import com.outgoings.service.UserService;
import java.util.Arrays;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(
    controllers = TransactionController.class,
    excludeFilters =
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = com.outgoings.security.SecurityConfiguration.class))
class TransactionControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private TransactionService transactionService;

  @MockBean private UserService userService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  void addTransaction_shouldReturnSavedTransaction() throws Exception {

    Account account = new Account();
    account.setId(1);
    account.setUsername("testUser");

    Transaction tx = new Transaction();
    tx.setId(10);
    tx.setTitle("Test");
    tx.setAmount(200.0);
    tx.setDate(new Date());

    Mockito.when(userService.findByUsername("testUser")).thenReturn(account);
    Mockito.when(transactionService.addTransaction(Mockito.any(), Mockito.any())).thenReturn(tx);

    mockMvc
        .perform(
            post("/api/transactions")
                .with(csrf()) // CSRF token
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tx)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(10))
        .andExpect(jsonPath("$.title").value("Test"));
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  void getTransactionsShouldReturnTransactions() throws Exception {
    Account account = new Account();
    account.setId(1);
    account.setUsername("testUser");

    Transaction t1 = new Transaction();
    t1.setId(10);
    t1.setTitle("T1");

    Transaction t2 = new Transaction();
    t2.setId(20);
    t2.setTitle("T2");

    Mockito.when(userService.findByUsername("testUser")).thenReturn(account);
    Mockito.when(transactionService.getTransactions(account)).thenReturn(Arrays.asList(t1, t2));

    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get(
                "/api/transactions"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(10))
        .andExpect(jsonPath("$[1].id").value(20));
  }
}
