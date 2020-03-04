package com.outgoings.repository;

import com.outgoings.entity.Money;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyRepository extends CrudRepository<Money, Integer> {
}
