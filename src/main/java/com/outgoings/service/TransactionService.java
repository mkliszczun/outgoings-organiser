package com.outgoings.service;

import com.outgoings.entity.Account;
import com.outgoings.entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {
    Transaction addTransaction(Account account, Transaction transaction);
    void deleteTransaction(Transaction transaction);
    Transaction editTransaction(Account account, Transaction transaction);
    List<Transaction> getTransactions(Account account);
    void clearTransactions(Account account);
}
