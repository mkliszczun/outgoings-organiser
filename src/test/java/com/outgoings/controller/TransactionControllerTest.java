package com.outgoings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outgoings.entity.Account;
import com.outgoings.entity.Transaction;
import com.outgoings.service.TransactionService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(
        controllers = TransactionController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = com.outgoings.security.SecurityConfiguration.class
        )
)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

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
        Mockito.when(transactionService.addTransaction(Mockito.any(), Mockito.any()))
                .thenReturn(tx);

        mockMvc.perform(post("/api/transactions")
                        .with(csrf())                                 // CSRF token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tx)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("Test"));
    }
    @Test

    void getTransactionsShouldReturnTransactions() throws Exception{

    }
}