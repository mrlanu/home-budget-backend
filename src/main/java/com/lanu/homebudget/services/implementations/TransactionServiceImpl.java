package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Account;
import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.exceptions.ResourceNotFoundException;
import com.lanu.homebudget.repositories.TransactionRepository;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.services.AccountService;
import com.lanu.homebudget.services.TransactionService;
import com.lanu.homebudget.views.TransactionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public ResponseEntity<?> deleteTransaction(Long transactionId) {
        if(!transactionRepository.existsById(transactionId)) {
            throw new ResourceNotFoundException("TransactionId " + transactionId + " not found");
        }

        return transactionRepository.findById(transactionId).map(transaction -> {
            Account account = transaction.getAccount();
            account.setBalance(account.getBalance() - transaction.getAmount());
            accountService.saveAccount(account);
            transactionRepository.delete(transaction);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("transactionId " + transactionId + " not found"));
    }

    @Override
    public List<TransactionView> findAllByUserAndDateBetween(User user, Date date) {

        LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime localDateStart = localDate.withDayOfMonth(1);
        LocalDateTime localDateEnd = localDate.plusMonths(1).withDayOfMonth(1).minusDays(1);

        return transactionRepository.findAllByUserAndDateBetween(user, localDateStart, localDateEnd)
                .stream()
                .map(transaction -> new TransactionView(
                        transaction.getId(),
                        transaction.getDate(),
                        transaction.getType().toString(),
                        transaction.getDescription(),
                        transaction.getAmount(),
                        transaction.getCategory().getName(),
                        transaction.getSubCategory().getName(),
                        transaction.getAccount().getName(),
                        transaction.getAccount().getType()))
                .collect(Collectors.toList());
    }

    @Override
    public Transaction createTransaction(User user, Transaction transaction) {
        transaction.setUser(user);
        Account account = accountService.findAccountById(transaction.getAccount().getId());

        account.setBalance(account.getBalance() + transaction.getAmount());

        accountService.saveAccount(account);
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("TransactionId " + transactionId + " not found"));
    }

    @Override
    public Transaction editTransaction(Transaction transactionRequest){
        if(!transactionRepository.existsById(transactionRequest.getId())) {
            throw new ResourceNotFoundException("TransactionId " + transactionRequest.getId() + " not found");
        }

        return transactionRepository.findById(transactionRequest.getId()).map(transaction -> {

            Account account = accountService.findAccountById(transaction.getAccount().getId());
            account.setBalance(account.getBalance() - transaction.getAmount());
            accountService.saveAccount(account);

            account = accountService.findAccountById(transactionRequest.getAccount().getId());
            account.setBalance(account.getBalance() + transactionRequest.getAmount());
            accountService.saveAccount(account);

            transaction.setAccount(transactionRequest.getAccount());
            transaction.setCategory(transactionRequest.getCategory());
            transaction.setSubCategory(transactionRequest.getSubCategory());
            transaction.setAmount(transactionRequest.getAmount());
            transaction.setDate(transactionRequest.getDate());
            transaction.setDescription(transactionRequest.getDescription());
            transaction.setType(transactionRequest.getType());
            return transactionRepository.save(transaction);
        }).orElseThrow(() -> new ResourceNotFoundException("TransactionId " + transactionRequest.getId() + "not found"));
    }

}
