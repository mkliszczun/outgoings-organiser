package com.outgoings.service;

import com.outgoings.entity.Account;
import com.outgoings.repository.AccountRepository;
import com.outgoings.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {
    @Mock private TransactionRepository transactionRepository;
    @Mock private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl underTest;
    private Account account;

    @BeforeEach
    void setUp(){
        account = new Account();
        account.setId(1);
        account.setUsername("test");
    }
}
