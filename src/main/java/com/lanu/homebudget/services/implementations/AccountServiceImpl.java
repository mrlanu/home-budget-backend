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
        account.setBalance(account.getInitialBalance());
        return accountRepository.save(account);
    }

    @Override
    public Account editAccount(Account accountRequest) {
        return accountRepository.findById(accountRequest.getId()).map(account -> {

            double newBalance = account.getBalance() - account.getInitialBalance() + accountRequest.getInitialBalance();

            account.setBalance(newBalance);
            account.setInitialBalance(accountRequest.getInitialBalance());
            account.setType(accountRequest.getType());
            account.setName(accountRequest.getName());
            account.setCurrency(accountRequest.getCurrency());
            account.setIncludeInTotal(accountRequest.isIncludeInTotal());
            return accountRepository.save(account);
        }).orElseThrow(() -> new ResourceNotFoundException("AccountId " + accountRequest.getId() + "not found"));
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
    public ResponseEntity<?> deleteAccount(Long accountId) {
        return accountRepository.findById(accountId).map(account -> {
            accountRepository.delete(account);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("AccountId " + accountId + " not found"));
    }
}
