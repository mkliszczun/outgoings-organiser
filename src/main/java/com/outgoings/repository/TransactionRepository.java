package com.outgoings.repository;

import com.outgoings.entity.Account;
import com.outgoings.entity.Transaction;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
  List<Transaction> findByAccount(Account account);
}
