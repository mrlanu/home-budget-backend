package com.lanu.homebudget.repositories;

import com.lanu.homebudget.entities.Transfer;
import com.lanu.homebudget.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    /*List<Transfer> findAllByUserAndDateBetween(User user, LocalDateTime start, LocalDateTime end);*/

    List<Transfer> findAllByBudget_IdAndDateBetween(Long budgetId, LocalDateTime start, LocalDateTime end);
}
