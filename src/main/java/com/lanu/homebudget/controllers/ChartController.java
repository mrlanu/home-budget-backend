package com.lanu.homebudget.controllers;

import com.lanu.homebudget.security.User;
import com.lanu.homebudget.security.UserService;
import com.lanu.homebudget.services.ChartService;
import com.lanu.homebudget.views.YearMonthSum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/charts")
public class ChartController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChartService chartService;

    @GetMapping("/sumsOfIncomesExpensesForYearByMonth")
    public List<YearMonthSum> getSumsOfIncomesExpensesForYearByMonth(Principal principal){
        User user = userService.findByUsername(principal.getName()).get();
        return chartService.getSumsOfIncomesExpensesForYearByMonth(user);
    }
}
