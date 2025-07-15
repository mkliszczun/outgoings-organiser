package com.outgoings.service;

import com.outgoings.entity.Account;
import com.outgoings.entity.Transaction;
import com.outgoings.repository.TransactionRepository;
import com.outgoings.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void addTransaction_shouldSaveTransaction() {
        Account account = new Account();
        account.setId(1);
        account.setUsername("testUser");

        Transaction transaction = new Transaction();
        transaction.setTitle("Test transaction");
        transaction.setAmount(150.0);
        transaction.setDate(new Date());
        transaction.setAccount(account);

        Mockito.when(transactionRepository.findByAccount(account))
                .thenReturn(new ArrayList<>());

        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class)))
                .thenAnswer(invocation -> {
                    Transaction t = invocation.getArgument(0);
                    t.setId(10);
                    return t;
                });

        Transaction result = transactionService.addTransaction(account, transaction);

        assertNotNull(result);
        assertEquals(10, result.getId());
        assertEquals("Test transaction", result.getTitle());
        assertEquals(account, result.getAccount());

        Mockito.verify(transactionRepository).findByAccount(account);
        Mockito.verify(transactionRepository).save(transaction);
    }

    @Test
    void getTransactions_shouldReturnTransactionsList() {
        Account account = new Account();
        account.setId(1);
        Transaction t1 = new Transaction();
        t1.setId(10);
        Transaction t2 = new Transaction();
        t2.setId(20);

        List<Transaction> transactions = Arrays.asList(t1, t2);

        Mockito.when(transactionRepository.findByAccount(account)).thenReturn(transactions);

        List<Transaction> result = transactionService.getTransactions(account);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(t1));
        assertTrue(result.contains(t2));
    }

    @Test
    void clearTransactions_shouldDeleteAllTransactions() {
        Account account = new Account();
        account.setId(1);
        Transaction t1 = new Transaction();
        t1.setId(10);
        Transaction t2 = new Transaction();
        t2.setId(20);

        List<Transaction> transactions = new ArrayList<>(Arrays.asList(t1, t2));

        Mockito.when(transactionRepository.findByAccount(account)).thenReturn(transactions);

        transactionService.clearTransactions(account);

        Mockito.verify(transactionRepository, Mockito.times(2)).delete(Mockito.any(Transaction.class));
    }

    @Test
    void getById_shouldReturnTransactionWhenExists() {
        Transaction t = new Transaction();
        t.setId(42);

        Mockito.when(transactionRepository.findById(42)).thenReturn(Optional.of(t));

        Transaction result = transactionService.getById(42);

        assertNotNull(result);
        assertEquals(42, result.getId());
    }

    @Test
    void getById_shouldReturnNullWhenNotFound() {
        Mockito.when(transactionRepository.findById(99)).thenReturn(Optional.empty());

        Transaction result = transactionService.getById(99);

        assertNull(result);
    }

}
