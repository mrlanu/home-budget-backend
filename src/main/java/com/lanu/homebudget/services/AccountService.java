package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Account;
import com.lanu.homebudget.security.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    Account createAccount(User user, Account account);
    List<Account> findAccountsByUser(User user);
    Account findAccountById(Long accountId);
    Account updateAccount(Long accountId, Account account);
    ResponseEntity<?> deleteAccount(Long accountId);
    Account saveAccount(Account account);
}
