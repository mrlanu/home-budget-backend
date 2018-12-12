package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.repositories.TransactionRepository;
import com.lanu.homebudget.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SummaryServiceImpl implements SummaryService{

   @Autowired
   private TransactionRepository transactionRepository;

    @Override
    public Map<String, List<Transaction>> getSummaryForTransactionsType(User user, Transaction.TransactionType type) {
        List<Transaction> transactionList = transactionRepository.findAllByUserAndType(user, type);
        return transactionList
                .stream()
                .collect(Collectors.groupingBy(transaction -> transaction.getCategory().getName()));
    }
}
