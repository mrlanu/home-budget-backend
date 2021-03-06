package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Account;
import com.lanu.homebudget.entities.Category;
import com.lanu.homebudget.entities.SubCategory;
import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.repositories.TransactionRepository;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.services.AccountService;
import com.lanu.homebudget.services.SummaryService;
import com.lanu.homebudget.views.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public List<Group> getSummaryByCategory(Long budgetId, Date date, Transaction.TransactionType type) {

        LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime localDateStart = localDate.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime localDateEnd = localDate.plusMonths(1).withDayOfMonth(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);

        List<Group> result = new ArrayList<>();

        List<Transaction> transactionList = transactionRepository
                .findAllByBudget_IdAndDateBetweenAndType(budgetId, localDateStart, localDateEnd, type);

        Map<Category, Map<SubCategory, List<Transaction>>> groupedByCategory = transactionList
                .stream()
                .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.groupingBy(Transaction::getSubCategory)));

        groupedByCategory.forEach((key,value) -> result.add(new Group(
                key.getId(),
                key.getName(),
                getListOfGroupSubcategories(value),
                getListOfGroupSubcategories(value)
                        .stream()
                        .mapToDouble(GroupSubcategory::getSpent)
                        .sum()
        )));
        return result;
    }

    private List<GroupSubcategory> getListOfGroupSubcategories (Map<SubCategory, List<Transaction>> map) {
        List<GroupSubcategory> result = new ArrayList<>();
        map.forEach((k,v) -> result.add(new GroupSubcategory(
                k.getId(),
                k.getName(),
                v.stream()
                        .mapToDouble(Transaction::getAmount)
                        .sum())
        ));
        return result;
    }

    public List<GroupAccount> getSummaryOfAccounts(Long budgetId) {
        List<Account> accountList = accountService.findAccountsByBudgetId(budgetId);
        List<GroupAccount> result = new ArrayList<>();

        Map<String, List<Account>> groupedByType = accountList.stream()
                .collect(Collectors.groupingBy(Account::getType));

        groupedByType.forEach((key, value) -> result.add(
                new GroupAccount(
                        key,
                        value,
                        value.stream().mapToDouble(Account::getBalance).sum()
                )));

        return result;
    }

    @Override
    public Brief getBrief(Long budgetId) {

        Date date = new Date();
        double accountsTotal;

        LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime localDateStart = localDate.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime localDateEnd = localDate.plusMonths(1).withDayOfMonth(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);

        List<Transaction> transactionList = transactionRepository
                .findAllByBudget_IdAndDateBetween(budgetId, localDateStart, localDateEnd);

        Map<Transaction.TransactionType, Double> transactionTypeDoubleMap =
                transactionList
                        .stream()
                        .collect(Collectors.groupingBy(
                                Transaction::getType, Collectors.summingDouble(Transaction::getAmount)));

        accountsTotal = accountService.findAccountsByBudgetId(budgetId)
                .stream()
                .filter(account -> account.isIncludeInTotal())
                .mapToDouble(Account::getBalance)
                .sum();

        return new Brief(accountsTotal,
                transactionTypeDoubleMap.getOrDefault(Transaction.TransactionType.EXPENSE, 0.0),
                transactionTypeDoubleMap.getOrDefault(Transaction.TransactionType.INCOME, 0.0));
    }
}
