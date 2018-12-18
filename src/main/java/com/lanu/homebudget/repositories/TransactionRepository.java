package com.lanu.homebudget.repositories;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByUser(User user);
    List<Transaction> findAllByUserAndDateBetween(User user, LocalDate start, LocalDate end);
    List<Transaction> findAllByUserAndDateBetweenAndType(User user,
                                                         LocalDate start, LocalDate end,
                                                         Transaction.TransactionType transactionType);

}
