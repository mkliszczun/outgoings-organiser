package com.outgoings.service;

import com.outgoings.entity.Account;
import com.outgoings.entity.Transaction;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
  Transaction addTransaction(Account account, Transaction transaction);

  void deleteTransaction(Transaction transaction);

  Transaction getById(int id);

  Transaction editTransaction(Account account, Transaction transaction);

  List<Transaction> getTransactions(Account account);

  void clearTransactions(Account account);
}
