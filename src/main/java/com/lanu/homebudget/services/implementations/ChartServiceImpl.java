package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.repositories.TransactionRepository;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.services.ChartService;
import com.lanu.homebudget.views.YearMonthSum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChartServiceImpl implements ChartService {

    private LocalDateTime today = LocalDateTime.now();
    private LocalDateTime dateEnd = today.withDayOfMonth(1).plusMonths(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);
    private LocalDateTime dateStart = dateEnd.minusYears(1).plusDays(1).withHour(0).withMinute(0).withSecond(0);
    private YearMonth start = YearMonth.of(dateStart.getYear(), dateStart.getMonthValue());

    @Autowired
    private TransactionRepository transactionRepository;

    public List<YearMonthSum> getSumsOfIncomesExpensesForYearByMonth(Long budgetId) {

        List<YearMonthSum> result = new ArrayList<>();

        List<Transaction> transactionList = transactionRepository
                .findAllByBudget_IdAndDateBetween(budgetId, dateStart, dateEnd);

        Map<Transaction.TransactionType, Map<YearMonth, Double>> yearMonthDoubleMap = transactionList
                .stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors.groupingBy(Transaction::getType,
                        Collectors.groupingBy(e -> YearMonth.of(e.getDate().getYear(), e.getDate().getMonth().getValue()),
                                Collectors.summingDouble(Transaction::getAmount))));

        for (int i = 0; i <= 11; i++) {
            YearMonthSum y = new YearMonthSum(YearMonth.of(start.getYear(), start.getMonthValue()).plusMonths(i), 0.0, 0.0);
            y.setExpenseSum(yearMonthDoubleMap.get(Transaction.TransactionType.EXPENSE).getOrDefault(y.getDate(), 0.0));
            y.setIncomeSum(yearMonthDoubleMap.get(Transaction.TransactionType.INCOME).getOrDefault(y.getDate(), 0.0));
            result.add(y);
        }

        return result;
    }


    public List<YearMonthSum> getSumsByCategoryAndMonth(Long transactionId) {

        List<YearMonthSum> result = new ArrayList<>();

        List<Transaction> transactionList = transactionRepository
                .findAllByCategory_IdAndDateBetween(transactionId, dateStart, dateEnd);

        Map<YearMonth, Double> yearMonthDoubleMap = transactionList.stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors
                        .groupingBy(e -> YearMonth.of(e.getDate().getYear(), e.getDate().getMonth().getValue()),
                                Collectors.summingDouble(Transaction::getAmount)));

        for (int i = 0; i <= 11; i++) {
            YearMonthSum y = new YearMonthSum(YearMonth.of(start.getYear(), start.getMonthValue()).plusMonths(i), 0.0, 0.0);
            y.setExpenseSum(yearMonthDoubleMap.getOrDefault(y.getDate(), 0.0));
            result.add(y);
        }

        return result;
    }
}