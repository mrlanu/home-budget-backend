package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Budget;
import com.lanu.homebudget.exceptions.ResourceNotFoundException;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.security.UserService;
import com.lanu.homebudget.services.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private UserService userService;

    @GetMapping("/budgets")
    public List<Budget> getBudgetsByUser(Principal principal){
        User user = userService.findByUsername(principal.getName()).get();
        return budgetService.getBudgetByUser(user);
    }

    @PostMapping("/budgets")
    public Budget createBudget(@Valid @RequestBody Budget budget, Principal principal){
        User user = userService.findByUsername(principal.getName()).get();
        return budgetService.createBudget(user, budget);
    }

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

    @DeleteMapping("/budgets/{budgetId}")
    public ResponseEntity<?> deleteBudget(@PathVariable(value = "budgetId") Long budgetId){
        return budgetService.deleteBudget(budgetId);
    }
}
