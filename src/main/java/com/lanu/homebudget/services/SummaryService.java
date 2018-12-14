package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.views.Group;

import java.util.Date;
import java.util.List;

public interface SummaryService {
    List<Group> getSummaryByCategory(User user, Date date, Transaction.TransactionType type);
}
