package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.repositories.TransactionRepository;
import com.lanu.homebudget.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> findAllByUser(User user) {
        return transactionRepository.findAllByUser(user);
    }

    @Override
    public Transaction createTransaction(User user, Transaction transaction) {
        transaction.setUser(user);
        return transactionRepository.save(transaction);
    }
}
