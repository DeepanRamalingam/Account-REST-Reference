package com.deepan.repository;

import com.deepan.model.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account,Integer> {

    Optional<Account> findByAccountNumber(String accountNumber);
}
