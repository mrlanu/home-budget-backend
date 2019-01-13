package com.lanu.homebudget.security;

import com.lanu.homebudget.entities.Budget;
import com.lanu.homebudget.exceptions.ResourceNotFoundException;
import com.lanu.homebudget.repositories.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class SecurityController {

    @Autowired
    private UserService userService;

    @Autowired
    private BudgetRepository budgetRepository;

    @PostMapping("/signup")
    public User registerUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/user")
    public User getLoggedInUser(Principal principal){
        return userService.findByUsername(principal.getName()).get();
    }

    @GetMapping("/budgets/{budgetId}/users")
    public List<User> getUsersByBudgetId(@PathVariable(value = "budgetId")Long budgetId){
        return budgetRepository.findById(budgetId).map(Budget::getUserList)
                .orElseThrow(() -> new ResourceNotFoundException("BudgetId " + budgetId + " not found"));
    }
}
