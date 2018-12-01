package com.lanu.homebudget.repositories;

import com.lanu.homebudget.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
