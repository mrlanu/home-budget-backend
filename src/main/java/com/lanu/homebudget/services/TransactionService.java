package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.security.User;

import java.util.List;

public interface TransactionService {

    List<Transaction> findAllByUser(User user);
    Transaction createTransaction(User user, Transaction transaction);
}
