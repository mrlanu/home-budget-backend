package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Debt;
import com.lanu.homebudget.views.DebtStrategyReport;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DebtPayoffService {
    Debt createDebt(Long budgetId, Debt debt);
    List<Debt> getAllDebtsByBudgetId(Long budgetId);
    List<DebtStrategyReport> countDebtsPayOffStrategy(Long budgetId, double extra, String strategy);
    ResponseEntity<?> deleteDebt(Long debtId);
}
