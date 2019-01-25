package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Account;
import com.lanu.homebudget.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<Account> getAllAccounts(@RequestParam(name = "budgetId") Long budgetId){
        return accountService.findAccountsByBudgetId(budgetId);
    }

    @GetMapping("/{accountId}")
    public Account getAccountById(@PathVariable(value = "accountId") Long accountId){
        return accountService.findAccountById(accountId);
    }

    @PostMapping
    public Account createAccount(@RequestParam(name = "budgetId") Long budgetId,
                                 @Valid @RequestBody Account account){
        return accountService.createAccount(budgetId, account);
    }

    @PutMapping
    public Account updateAccount(@Valid @RequestBody Account accountRequest) {
        return accountService.editAccount(accountRequest);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable (value = "accountId") Long accountId) {
        return accountService.deleteAccount(accountId);
    }
}
