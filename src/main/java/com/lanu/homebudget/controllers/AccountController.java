package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Account;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.security.UserService;
import com.lanu.homebudget.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<Account> getAllAccounts(Principal principal){
        User user = userService.findByUsername(principal.getName()).get();
        return accountService.findAccountsByUser(user);
    }

    @GetMapping("/{accountId}")
    public Account getAccountById(@PathVariable(value = "accountId") Long accountId){
        return accountService.findAccountById(accountId);
    }

    @PostMapping
    public Account createAccount(Principal principal,
                                 @Valid @RequestBody Account account){
        User user = userService.findByUsername(principal.getName()).get();
        return accountService.createAccount(user, account);
    }

    @PutMapping("/{accountId}")
    public Account updateAccount(@PathVariable (value = "accountId") Long accountId,
                               @Valid @RequestBody Account accountRequest) {
        return accountService.updateAccount(accountId, accountRequest);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable (value = "accountId") Long accountId) {
        return accountService.deleteAccount(accountId);
    }
}
