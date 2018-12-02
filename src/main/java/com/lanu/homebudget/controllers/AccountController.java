package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Account;
import com.lanu.homebudget.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/users/{userId}/accounts")
    public List<Account> getAllAccounts(@PathVariable(value = "userId") Long userId){
        return accountService.findAccountsByUserId(userId);
    }

    @GetMapping("/accounts/{accountId}")
    public Account getAccountById(@PathVariable(value = "accountId") Long accountId){
        return accountService.findAccountById(accountId);
    }

    @PostMapping("/users/{userId}/accounts")
    public Account createAccount(@PathVariable(value = "userId") Long userId,
                                 @Valid @RequestBody Account account){
        return accountService.createAccount(userId, account);
    }

    @PutMapping("/accounts/{accountId}")
    public Account updateAccount(@PathVariable (value = "accountId") Long accountId,
                               @Valid @RequestBody Account accountRequest) {
        return accountService.updateAccount(accountId, accountRequest);
    }

    @DeleteMapping("/accounts/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable (value = "accountId") Long accountId) {
        return accountService.deleteAccount(accountId);
    }
}
