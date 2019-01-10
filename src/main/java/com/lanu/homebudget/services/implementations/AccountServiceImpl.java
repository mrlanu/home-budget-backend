package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Account;
import com.lanu.homebudget.exceptions.ResourceNotFoundException;
import com.lanu.homebudget.repositories.AccountRepository;
import com.lanu.homebudget.repositories.BudgetRepository;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Override
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account createAccount(Long budgetId, Account account) {
        account.setBudget(budgetRepository.findById(budgetId).get());
        return accountRepository.save(account);
    }

    @Override
    public List<Account> findAccountsByBudgetId(Long budgetId) {
        return accountRepository.findAllByBudget_Id(budgetId);
    }

    @Override
    public Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("AccountId " + accountId + " not found"));
    }

    @Override
    public Account updateAccount(Long accountId, Account requestAccount) {
        return accountRepository.findById(accountId).map(account -> {
            account.setName(requestAccount.getName());
            account.setBalance(requestAccount.getBalance());
            account.setCurrency(requestAccount.getCurrency());
            account.setIncludeInTotal(requestAccount.isIncludeInTotal());
            return accountRepository.save(account);
        }).orElseThrow(() -> new ResourceNotFoundException("AccountId " + accountId + "not found"));
    }

    @Override
    public ResponseEntity<?> deleteAccount(Long accountId) {
        return accountRepository.findById(accountId).map(account -> {
            accountRepository.delete(account);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("AccountId " + accountId + " not found"));
    }
}
