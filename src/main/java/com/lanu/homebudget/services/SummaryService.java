package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.views.Brief;
import com.lanu.homebudget.views.Group;
import com.lanu.homebudget.views.GroupAccount;

import java.util.Date;
import java.util.List;

public interface SummaryService {
    List<Group> getSummaryByCategory(Long budgetId, Date date, Transaction.TransactionType type);
    List<GroupAccount> getSummaryOfAccounts(Long budgetId);
    Brief getBrief(Long budgetId);
}
