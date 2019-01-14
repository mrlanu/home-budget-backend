package com.lanu.homebudget.services;

import com.lanu.homebudget.security.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BudgetService {

    List<User> getUsersByBudgetId(Long budgetId);
    ResponseEntity<?> addUserToBudget(Long budgetId, String userName);
}
