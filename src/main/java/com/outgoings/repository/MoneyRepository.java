package com.outgoings.repository;

import com.outgoings.entity.Account;
import com.outgoings.entity.Money;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyRepository extends CrudRepository<Money, Integer> {
  Optional<List<Money>> findByAccount(Account account);
}
