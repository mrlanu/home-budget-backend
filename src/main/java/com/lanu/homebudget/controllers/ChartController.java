package com.lanu.homebudget.controllers;

import com.lanu.homebudget.services.ChartService;
import com.lanu.homebudget.views.YearMonthSum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/charts")
public class ChartController {

    @Autowired
    private ChartService chartService;

    @GetMapping("/sumsOfIncomesExpensesForYearByMonth")
    public List<YearMonthSum> getSumsOfIncomesExpensesForYearByMonth(@RequestParam(name = "budgetId") Long budgetId){
        return chartService.getSumsOfIncomesExpensesForYearByMonth(budgetId);
    }

    @GetMapping("/spentMonthToMonthByCategory")
    public List<YearMonthSum> getSpentMonthToMonthByCategory(@RequestParam(name = "categoryId")Long categoryId){
        return chartService.getSumsByCategoryAndMonth(categoryId);
    }
}
