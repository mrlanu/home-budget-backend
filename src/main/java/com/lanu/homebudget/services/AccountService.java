package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Account;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    Account createAccount(Long userId, Account account);
    List<Account> findAccountsByUserId(Long userId);
    Account findAccountById(Long accountId);
    Account updateAccount(Long accountId, Account account);
    ResponseEntity<?> deleteAccount(Long accountId);
}
