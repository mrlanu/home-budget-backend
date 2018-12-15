package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Account;
import com.lanu.homebudget.entities.Category;
import com.lanu.homebudget.entities.SubCategory;
import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.repositories.TransactionRepository;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.services.AccountService;
import com.lanu.homebudget.services.SummaryService;
import com.lanu.homebudget.views.Group;
import com.lanu.homebudget.views.GroupAccount;
import com.lanu.homebudget.views.GroupSubcategory;
import com.lanu.homebudget.views.TransactionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public List<Group> getSummaryByCategory(User user, Date date, Transaction.TransactionType type) {

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDateStart = localDate.withDayOfMonth(1);
        LocalDate localDateEnd = localDate.plusMonths(1).withDayOfMonth(1).minusDays(1);

        List<Group> result = new ArrayList<>();

        List<Transaction> transactionList = transactionRepository
                .findAllByUserAndDateBetweenAndType(user, localDateStart, localDateEnd, type);

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
                            .sum(),
                    v.stream()
                            .map(transaction -> new TransactionView(
                            transaction.getId(),
                            transaction.getDate(),
                            transaction.getDescription(),
                            transaction.getAmount(),
                            transaction.getCategory().getName(),
                            transaction.getSubCategory().getName(),
                            transaction.getAccount().getName()))
                            .collect(Collectors.toList()))
        ));
        return result;
    }

    public List<GroupAccount> getSummaryOfAccounts(User user) {
        List<Account> accountList = accountService.findAccountsByUser(user);
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
}