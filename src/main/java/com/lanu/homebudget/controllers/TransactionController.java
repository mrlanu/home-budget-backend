package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.services.TransactionService;
import com.lanu.homebudget.services.TransactionViewService;
import com.lanu.homebudget.views.TransactionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionViewService transactionViewService;

    @GetMapping("/transactions")
    public List<TransactionView> getAllTransactions(@RequestParam(name = "budgetId") Long budgetId, @RequestParam(name = "date") Date date){
        return transactionViewService.mappingTransactionsAndTransfersToTransactionView(budgetId, date);
    }

    @GetMapping("/transactions/{transactionId}")
    public Transaction getTransaction(@PathVariable(name = "transactionId")Long transactionId){
        return transactionService.getTransactionById(transactionId);
    }

    @PostMapping("/transactions")
    public Transaction createTransaction(@RequestParam(name = "budgetId") Long budgetId,
                                         @Valid @RequestBody Transaction transaction){
        return transactionService.createTransaction(budgetId, transaction);
    }

    @PutMapping("/transactions")
    public Transaction updateTransaction(@Valid @RequestBody Transaction transaction) {
        return transactionService.editTransaction(transaction);
    }

    @DeleteMapping("/transactions")
    public ResponseEntity<?> deleteTransaction(@RequestParam(name = "transactionId") Long transactionId) {
        return transactionService.deleteTransaction(transactionId);
    }
}
