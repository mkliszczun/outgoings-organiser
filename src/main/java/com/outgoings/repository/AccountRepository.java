package com.outgoings.repository;

import com.outgoings.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {

  @Query("SELECT a FROM Account a where a.username = ?1 and a.password = ?2 ")
  Optional<Account> login(String username, String password);

  Optional<Account> findByToken(String token);

  Optional<Account> findByUsername(String username);
}
