package com.lanu.homebudget.repositories;

import com.lanu.homebudget.entities.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
