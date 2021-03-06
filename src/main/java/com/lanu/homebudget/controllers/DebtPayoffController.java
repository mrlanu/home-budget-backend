package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Debt;
import com.lanu.homebudget.services.DebtPayoffService;
import com.lanu.homebudget.views.DebtPayoffStrategy;
import com.lanu.homebudget.views.DebtStrategyReport;
import org.springframework.http.ResponseEntity;
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
    public DebtPayoffStrategy getReport(@RequestParam(name = "budgetId") Long budgetId,
                                        @RequestParam(name = "extraPayment") double extra,
                                        @RequestParam(name = "strategy") String strategy){
        return debtPayoffService.countDebtsPayOffStrategy(budgetId, extra, strategy);
    }

    @PutMapping
    public Debt updateDebt(@Valid @RequestBody Debt debt) {
        return debtPayoffService.editDebt(debt);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDebt(@RequestParam(value = "debtId") Long debtId) {
        return debtPayoffService.deleteDebt(debtId);
    }
}
