package com.lanu.homebudget.repositories;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByCategory_IdAndDateBetween(Long categoryId, LocalDateTime start, LocalDateTime end);
    List<Transaction> findAllByBudget_IdAndDateBetween(Long budgetId, LocalDateTime start, LocalDateTime end);
    List<Transaction> findAllByBudget_IdAndDateBetweenAndType(Long budgetId,
                                                         LocalDateTime start, LocalDateTime end,
                                                         Transaction.TransactionType transactionType);
    Transaction findFirstByAccount_Id(Long accId);
    Transaction findFirstByCategory_Id(Long categoryId);
    Transaction findFirstBySubCategory_Id(Long subCategoryId);

}
