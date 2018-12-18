package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.security.UserService;
import com.lanu.homebudget.services.TransactionService;
import com.lanu.homebudget.views.TransactionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping("/transactions")
    public List<TransactionView> getAllTransactions(Principal principal,
                                                    @RequestParam(name = "date") Date date){
        User user = userService.findByUsername(principal.getName()).get();
        return transactionService.findAllByUserAndDateBetween(user, date);
    }

    @PostMapping("/transactions")
    public Transaction createTransaction(Principal principal,
                                         @Valid @RequestBody Transaction transaction){
        User user = userService.findByUsername(principal.getName()).get();
        return transactionService.createTransaction(user, transaction);
    }
}
