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
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SummaryServiceImpl implements SummaryService {

   @Autowired
   private TransactionRepository transactionRepository;

   @Autowired
   private AccountService accountService;

    @Override
    public List<Group> getSummaryByCategory(User user, Date date, Transaction.TransactionType type) {

        LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime localDateStart = localDate.withDayOfMonth(1);
        LocalDateTime localDateEnd = localDate.plusMonths(1).withDayOfMonth(1).minusDays(1);

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
                                    transaction.getType().toString(),
                                    transaction.getDescription(),
                                    transaction.getAmount(),
                                    transaction.getCategory().getName(),
                                    transaction.getSubCategory().getName(),
                                    transaction.getAccount().getName(),
                                    transaction.getAccount().getType()))
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

    @Override
    public Brief getBrief(User user) {

        Date date = new Date();
        double accountsTotal;

        LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime localDateStart = localDate.withDayOfMonth(1);
        LocalDateTime localDateEnd = localDate.plusMonths(1).withDayOfMonth(1).minusDays(1);

        List<Transaction> transactionList = transactionRepository
                .findAllByUserAndDateBetween(user, localDateStart, localDateEnd);

        Map<Transaction.TransactionType, Double> transactionTypeDoubleMap =
                transactionList
                .stream()
                .collect(Collectors.groupingBy(
                        Transaction::getType, Collectors.summingDouble(Transaction::getAmount)));

        accountsTotal = accountService.findAccountsByUser(user)
                .stream()
                .filter(account -> account.isIncludeInTotal())
                .mapToDouble(Account::getBalance)
                .sum();

        return new Brief(accountsTotal,
                transactionTypeDoubleMap.getOrDefault(Transaction.TransactionType.EXPENSE, 0.0),
                transactionTypeDoubleMap.getOrDefault(Transaction.TransactionType.INCOME, 0.0));
    }

    public List<YearMonthSum> getSumsOfIncomesExpensesForYearByMonth(User user) {

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime dateEnd = today.withDayOfMonth(1).plusMonths(1).minusDays(1);
        LocalDateTime dateStart = dateEnd.minusYears(1).plusDays(1);

        List<YearMonthSum> result = new ArrayList<>();
        YearMonthSum resultIncomes = new YearMonthSum(new ArrayList<>(), new ArrayList<>());
        YearMonthSum resultExspenses = new YearMonthSum(new ArrayList<>(), new ArrayList<>());

        List<Transaction> transactionList = transactionRepository
                .findAllByUserAndDateBetween(user, dateStart, dateEnd);

        Map<Transaction.TransactionType, Map<YearMonth, Double>> yearMonthDoubleMap = transactionList
                .stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors.groupingBy(Transaction::getType,
                        Collectors.groupingBy(e -> YearMonth.of(e.getDate().getYear(), e.getDate().getMonth().getValue()),
                                Collectors.summingDouble(Transaction::getAmount))));

        fillYearMonthSum(resultIncomes, yearMonthDoubleMap.get(Transaction.TransactionType.INCOME));
        fillYearMonthSum(resultExspenses, yearMonthDoubleMap.get(Transaction.TransactionType.EXPENSE));

        result.add(checkGapsInResultArray(resultIncomes));
        result.add(checkGapsInResultArray(resultExspenses));

        return result;
    }

    private void fillYearMonthSum(YearMonthSum yearMonthSum, Map<YearMonth, Double> map){
        map.forEach((k, v) -> {
            yearMonthSum.getDate().add(k);
            yearMonthSum.getSum().add(v < 0 ? v * -1 : v);
        });
    }

    // the method checks passed Array if it has any missed month and fill them with the sum equal 0.0
    private YearMonthSum checkGapsInResultArray(YearMonthSum yearMonthSum){

        YearMonth lastMonthInArray = yearMonthSum.getDate().get(yearMonthSum.getDate().size() -1);
        YearMonth monthShouldBe = lastMonthInArray.minusYears(1).plusMonths(1);

        for (int i = 0; i < yearMonthSum.getDate().size(); i++){
            YearMonth monthPresentInArray = yearMonthSum.getDate().get(i);

            if (monthPresentInArray.equals(monthShouldBe)){
                monthShouldBe = monthShouldBe.plusMonths(1);
            } else {
                yearMonthSum.getDate().add(i, monthShouldBe);
                yearMonthSum.getSum().add(i, 0.0);
                monthShouldBe = monthShouldBe.plusMonths(1);
            }
            if (monthPresentInArray.equals(lastMonthInArray)){
                break;
            }
        }
        return yearMonthSum;
    }

    /*public YearMonthSum getSumsByMonth(User user, Transaction.TransactionType transactionType) {

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime dateEnd = today.withDayOfMonth(1).plusMonths(1).minusDays(1);
        LocalDateTime dateStart = dateEnd.minusYears(1).plusDays(1);

        YearMonthSum result = new YearMonthSum(new ArrayList<>(), new ArrayList<>());

        List<Transaction> transactionList = transactionRepository
                .findAllByUserAndDateBetweenAndType(user, dateStart, dateEnd, transactionType);

        Map<YearMonth, Double> yearMonthDoubleMap = transactionList.stream()
                .filter(transaction -> transaction.getType() == transactionType)
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors
                        .groupingBy(e -> YearMonth.of(e.getDate().getYear(), e.getDate().getMonth().getValue()),
                                Collectors.summingDouble(Transaction::getAmount)));

        yearMonthDoubleMap.forEach((k, v) -> {
            result.getDate().add(k);
            result.getSum().add(v < 0 ? v * -1 : v);
        });

        List<YearMonthSum> yearMonthSumList = getSumsOfIncomesExpensesForYearByMonth(user);

        return checkGapsInResultArray(result);
    }*/
}
