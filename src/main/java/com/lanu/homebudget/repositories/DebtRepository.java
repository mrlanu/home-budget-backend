package com.lanu.homebudget.repositories;

import com.lanu.homebudget.entities.Debt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DebtRepository extends JpaRepository<Debt, Long> {
    List<Debt> findAllByBudget_Id(Long budgetId);
}
