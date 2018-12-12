package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.security.User;

import java.util.List;
import java.util.Map;

public interface SummaryService {
    Map<String, List<Transaction>> getSummaryForTransactionsType(User user, Transaction.TransactionType type);
}
