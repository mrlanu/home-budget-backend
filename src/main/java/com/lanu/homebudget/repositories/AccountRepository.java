package com.lanu.homebudget.repositories;

import com.lanu.homebudget.entities.Account;
import com.lanu.homebudget.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByUser(User user);
}
