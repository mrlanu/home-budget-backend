package com.lanu.homebudget.controllers;

import com.lanu.homebudget.security.User;
import com.lanu.homebudget.services.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping("/budgets/{budgetId}/users")
    public List<User> getUsersByBudgetId(@PathVariable(value = "budgetId")Long budgetId){
        return budgetService.getUsersByBudgetId(budgetId);
    }

    @GetMapping("/budgets/{budgetId}")
    public ResponseEntity<?> addUserToBudget(@PathVariable(value = "budgetId")Long budgetId,
                                             @RequestParam(name = "userName")String userName){
        return budgetService.addUserToBudget(budgetId, userName);
    }

    @GetMapping("/budgets/{budgetId}/removeUser")
    public ResponseEntity<?> removeUserFromBudget(@PathVariable(value = "budgetId")Long budgetId,
                                                  @RequestParam(name = "userName")String userName){
        return budgetService.removeUserFromBudget(budgetId, userName);
    }
}
