package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Budget;
import com.lanu.homebudget.security.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BudgetService {

    List<User> getUsersByBudgetId(Long budgetId);
    ResponseEntity<?> addUserToBudget(Long budgetId, String userName);
    ResponseEntity<?> removeUserFromBudget(Long budgetId, String userName);
    Budget createBudget(User user, Budget  budget);
}
