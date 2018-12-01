package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Account;
import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.exceptions.ResourceNotFoundException;
import com.lanu.homebudget.repositories.AccountRepository;
import com.lanu.homebudget.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/users/{userId}/accounts")
    public List<Account> getAllAccounts(@PathVariable(value = "userId") Long userId){
        return accountRepository.findAllByUser(userService.findByUserId(userId).get());
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public List<Transaction> getAllTransactionsByAccount(@PathVariable(value = "accountId") Long accountId){
        return accountRepository.findById(accountId).get().getTransactionList();
    }

    @PostMapping("/users/{userId}/accounts")
    public Account createAccount(@PathVariable(value = "userId") Long userId,
                                 @Valid @RequestBody Account account){
        return userService.findByUserId(userId).map(user -> {
            account.setUser(user);
            return accountRepository.save(account);
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }
}
