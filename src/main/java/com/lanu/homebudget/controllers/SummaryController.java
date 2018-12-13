package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.security.UserService;
import com.lanu.homebudget.services.SummaryService;
import com.lanu.homebudget.views.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/summaries")
public class SummaryController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private SummaryService summaryService;

    @GetMapping("/expenses")
    public List<Group> getExpensesSummary(Principal principal){
        User user = userService.findByUsername(principal.getName()).get();
        return summaryService.getAllExpenseGroups(user, Transaction.TransactionType.EXPENSE);
    }
}
