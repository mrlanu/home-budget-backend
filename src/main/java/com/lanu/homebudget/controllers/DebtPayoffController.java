package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Debt;
import com.lanu.homebudget.services.DebtPayoffService;
import com.lanu.homebudget.views.DebtStrategyReport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/debts")
public class DebtPayoffController {

    private DebtPayoffService debtPayoffService;

    public DebtPayoffController(DebtPayoffService debtPayoffService) {
        this.debtPayoffService = debtPayoffService;
    }

    @PostMapping("/payoff")
    public List<DebtStrategyReport> getReport(@Valid @RequestBody List<Debt> debtsList){
        return debtPayoffService.countDebtsPayOffStrategy(debtsList, 100);
    }
}
