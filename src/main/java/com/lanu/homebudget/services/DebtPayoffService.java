package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Debt;
import com.lanu.homebudget.views.DebtStrategyReport;

import java.util.List;

public interface DebtPayoffService {
    List<DebtStrategyReport> countDebtsPayOffStrategy(List<Debt> debts, double extra);
}
