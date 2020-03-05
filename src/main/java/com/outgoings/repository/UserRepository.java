package com.outgoings.repository;

import com.outgoings.entity.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Account, Integer> {

    @Query("SELECT a FROM Account a where a.username = ?1 and a.password = ?2 ")
    Optional<Account> login(String username, String password);
    Optional<Account> findByToken(String token);
    Optional<Account> findByUsername(String username);
}
