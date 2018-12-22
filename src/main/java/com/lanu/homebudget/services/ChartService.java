package com.lanu.homebudget.services;

import com.lanu.homebudget.security.User;
import com.lanu.homebudget.views.YearMonthSum;

import java.util.List;

public interface ChartService {
    /*YearMonthSum getSumsByMonth(User user, Transaction.TransactionType transactionType);*/
    List<YearMonthSum> getSumsOfIncomesExpensesForYearByMonth(User user);
    YearMonthSum getSumsByCategoryAndMonth(Long transactionId);
}
