package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.security.UserService;
import com.lanu.homebudget.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions(Principal principal){
        User user = userService.findByUsername(principal.getName()).get();
        return transactionService.findAllByUser(user);
    }

    @PostMapping("/transactions")
    public Transaction createTransaction(Principal principal, @Valid @RequestBody Transaction transaction){
        User user = userService.findByUsername(principal.getName()).get();
        return transactionService.createTransaction(user, transaction);
    }
}
