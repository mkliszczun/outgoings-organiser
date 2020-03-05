package com.outgoings.repository;

import com.outgoings.entity.Account;
import com.outgoings.entity.Money;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoneyRepository extends CrudRepository<Money, Integer> {
//    @Query("SLELCT m FROM Money m WHERE m.account")
    Optional<List<Money>> findByAccount(Account account);
}
