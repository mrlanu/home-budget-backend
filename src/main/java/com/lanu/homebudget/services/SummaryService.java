package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.views.Brief;
import com.lanu.homebudget.views.Group;
import com.lanu.homebudget.views.GroupAccount;
import com.lanu.homebudget.views.YearMonthSum;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SummaryService {
    List<Group> getSummaryByCategory(User user, Date date, Transaction.TransactionType type);
    List<GroupAccount> getSummaryOfAccounts(User user);
    Brief getBrief(User user);
    YearMonthSum getSumsByMonth(User user, Transaction.TransactionType transactionType);
}
