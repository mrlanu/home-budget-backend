package com.lanu.homebudget.controllers;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.security.UserService;
import com.lanu.homebudget.services.SummaryService;
import com.lanu.homebudget.views.Brief;
import com.lanu.homebudget.views.Group;
import com.lanu.homebudget.views.GroupAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/summaries")
public class SummaryController {
    
    @Autowired
    private SummaryService summaryService;

    @GetMapping("/categories")
    public List<Group> getSummaryByCategories(@RequestParam(name = "budgetId") Long budgetId,
                                              @RequestParam(name = "date") Date date,
                                              @RequestParam(name = "type") Transaction.TransactionType type){
        return summaryService.getSummaryByCategory(budgetId, date, type);
    }

    @GetMapping("/accounts")
    public List<GroupAccount> getSummaryByAccounts(@RequestParam(name = "budgetId") Long budgetId){
        return summaryService.getSummaryOfAccounts(budgetId);
    }

    @GetMapping("/brief")
    public Brief getBrief(@RequestParam(name = "budgetId") Long budgetId){
        return summaryService.getBrief(budgetId);
    }
}
