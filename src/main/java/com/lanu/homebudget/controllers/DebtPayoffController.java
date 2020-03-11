package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Debt;
import com.lanu.homebudget.services.DebtPayoffService;
import com.lanu.homebudget.views.DebtStrategyReport;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/debts")
public class DebtPayoffController {

    private DebtPayoffService debtPayoffService;

    public DebtPayoffController(DebtPayoffService debtPayoffService) {
        this.debtPayoffService = debtPayoffService;
    }

    @GetMapping
    public List<Debt> getAllDebtsByBudget(@RequestParam(name = "budgetId") Long budgetId){
        return debtPayoffService.getAllDebtsByBudgetId(budgetId);
    }

    @PostMapping
    public Debt createDebt(@RequestParam(name = "budgetId") Long budgetId,
                           @Valid @RequestBody Debt debt){
        return debtPayoffService.createDebt(budgetId, debt);
    }


    @GetMapping("/payoff")
    public List<DebtStrategyReport> getReport(@RequestParam(name = "budgetId") Long budgetId,
                                              @RequestParam(name = "extraPayment") double extra){
        return debtPayoffService.countDebtsPayOffStrategy(budgetId, extra);
    }
}
