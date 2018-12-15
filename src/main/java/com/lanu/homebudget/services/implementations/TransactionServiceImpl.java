package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Account;
import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.repositories.TransactionRepository;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.services.AccountService;
import com.lanu.homebudget.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public List<Transaction> findAllByUser(User user) {
        return transactionRepository.findAllByUser(user);
    }

    @Override
    public Transaction createTransaction(User user, Transaction transaction) {
        transaction.setUser(user);
        Account account = accountService.findAccountById(transaction.getAccount().getId());
        if (transaction.getType() == Transaction.TransactionType.EXPENSE){
            account.setBalance(account.getBalance() - transaction.getAmount());
        }else if (transaction.getType() == Transaction.TransactionType.INCOME){
            account.setBalance(account.getBalance() + transaction.getAmount());
        }
        accountService.saveAccount(account);
        return transactionRepository.save(transaction);
    }
}
