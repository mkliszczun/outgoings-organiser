package com.outgoings.service;

import com.outgoings.entity.Account;
import com.outgoings.entity.Transaction;
import com.outgoings.repository.AccountRepository;
import com.outgoings.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    TransactionRepository transactionRepository;
    AccountRepository accountRepository;

    @Override
    public Transaction addTransaction(Account account, Transaction transaction) {
        List<Transaction> transactions = getTransactions(account);
        transactions.add(transaction);
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(Transaction transaction) {
        transactionRepository.delete(transaction);
    }

    @Override
    public Transaction editTransaction(Account account, Transaction transaction) {

        return null;
    }

    @Override
    public List<Transaction> getTransactions(Account account) {
        return transactionRepository.findByAccount(account);
    }

    @Override
    public void clearTransactions(Account account) {
//        for(Transaction transaction : getTransactions(account)){
//            deleteTransaction(transaction);
//        }

        List<Transaction> transactions = getTransactions(account);
        for (Transaction transaction : transactions){
            deleteTransaction(transaction);
        }

        transactions.clear();

//        to avoid null transactions
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(0);
        transaction.setTitle("Initial transaction");
        transaction.setValue(account.getBaseValue());
        transaction.setDate(new Date());

        transactions.add(transaction);
        transactionRepository.save(transaction);

        accountRepository.save(account);
    }

    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}