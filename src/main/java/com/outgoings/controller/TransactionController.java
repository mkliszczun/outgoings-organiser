package com.outgoings.controller;

import com.outgoings.entity.Account;
import com.outgoings.entity.Transaction;
import com.outgoings.exception.ResourceNotFoundException;
import com.outgoings.service.TransactionService;
import com.outgoings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    TransactionService transactionService;
    UserService userService;

    @GetMapping
    List<Transaction> getAllTransactions(Authentication authentication){
        Account account = userService.findByUsername(authentication.getName());
        return transactionService.getTransactions(account);
    }

    @GetMapping("/{id}")
    List<Transaction> getTransaction(Authentication authentication, @PathVariable List<Integer> id){
        Account account = userService.findByUsername(authentication.getName());
        List<Transaction> userTransactions = transactionService.getTransactions(account);
        List<Transaction> desiredTransactions = new ArrayList<>();
        for (Transaction transaction : userTransactions){
            if (id.contains(transaction.getId()))desiredTransactions.add(transaction);
        }
        if (desiredTransactions.size()>0) return desiredTransactions;
        throw new ResourceNotFoundException("No transaction with given ID available for that user");
    }

    @GetMapping("/currency/{currency}")
    List getTransactionsByCurrencies(Authentication authentication, @PathVariable List<String> currency){
        Account account = userService.findByUsername(authentication.getName());
        List<Transaction> transactions = transactionService.getTransactions(account);

        return transactions.stream().filter(transaction -> currency.contains(transaction.getValue()))
                .collect(Collectors.toList());
    }

    @PostMapping
    Transaction addTransaction(Authentication authentication, @RequestBody Transaction transaction){
        Account account = userService.findByUsername(authentication.getName());
        transaction.setId(0);
        transaction.setDate(new Date());
        transaction.setAccount(account);

        return transactionService.addTransaction(account, transaction);
    }

    @DeleteMapping
    List<Transaction> clearTransactions(Authentication authentication){
        Account account = userService.findByUsername(authentication.getName());
        List<Transaction> transactions = transactionService.getTransactions(account);
        transactionService.clearTransactions(account);
        return transactions;
    }

    @DeleteMapping("/{id}")
    Transaction deleteTransactionById(Authentication authentication, @PathVariable int id){
        Account account = userService.findByUsername(authentication.getName());
        List<Transaction> transactions = transactionService.getTransactions(account);
        for (Transaction transaction : transactions){
            if (transaction.getId() == id) {
                transactionService.deleteTransaction(transaction);
                return transaction;
            }
        }
        throw new ResourceNotFoundException("No transaction with given ID available for that user");
    }

    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}