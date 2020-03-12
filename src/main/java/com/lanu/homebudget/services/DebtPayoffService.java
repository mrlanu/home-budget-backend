package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Debt;
import com.lanu.homebudget.views.DebtPayoffStrategy;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DebtPayoffService {
    Debt createDebt(Long budgetId, Debt debt);
    List<Debt> getAllDebtsByBudgetId(Long budgetId);
    DebtPayoffStrategy countDebtsPayOffStrategy(Long budgetId, double extra, String strategy);
    Debt editDebt(Debt debt);
    ResponseEntity<?> deleteDebt(Long debtId);
}
