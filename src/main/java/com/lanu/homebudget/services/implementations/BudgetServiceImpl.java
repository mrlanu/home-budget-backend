package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Budget;
import com.lanu.homebudget.exceptions.ResourceNotFoundException;
import com.lanu.homebudget.repositories.BudgetRepository;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.security.UserService;
import com.lanu.homebudget.services.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    private UserService userService;

    @Autowired
    private BudgetRepository budgetRepository;

    @Override
    public Budget createBudget(User user, Budget budgetReq) {
        Budget budget = budgetRepository.save(budgetReq);
        user.addBudget(budget);
        userService.saveUser(user);
        return budget;
    }

    @Override
    public List<Budget> getBudgetByUser(User user) {
        return user.getBudgets();
    }

    @Override
    public List<User> getUsersByBudgetId(Long budgetId) {
        return budgetRepository.findById(budgetId).map(Budget::getUserList)
                .orElseThrow(() -> new ResourceNotFoundException("BudgetId " + budgetId + " not found"));
    }

    @Override
    public ResponseEntity<?> addUserToBudget(Long budgetId, String userName) {
        return userService.findByUsername(userName).map(user -> {
            Budget budget = budgetRepository.findById(budgetId).get();
            user.addBudget(budget);
            userService.saveUser(user);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Username " + userName + " not found"));
    }

    @Override
    public ResponseEntity<?> removeUserFromBudget(Long budgetId, String userName) {
        return userService.findByUsername(userName).map(user -> {
            Budget budget = budgetRepository.findById(budgetId).get();
            user.getBudgets().remove(budget);
            userService.saveUser(user);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Username " + userName + " not found"));
    }
}
