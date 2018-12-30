package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.views.TransactionView;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface TransactionService {

    List<TransactionView> findAllByUserAndDateBetween(User user, Date date);
    Transaction createTransaction(User user, Transaction transaction);
    Transaction getTransactionById(Long id);
    ResponseEntity<?> deleteTransaction(Long transactionId);
    Transaction editTransaction(Transaction transactionRequest);
}
