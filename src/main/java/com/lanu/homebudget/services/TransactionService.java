package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.views.TransactionView;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface TransactionService {

    List<Transaction> findAllByBudgetIdAndDateBetween(Long budgetId, Date date);
    Transaction createTransaction(Long budgetId, Transaction transaction);
    Transaction getTransactionById(Long id);
    ResponseEntity<?> deleteTransaction(Long transactionId);
    Transaction editTransaction(Transaction transactionRequest);
}
