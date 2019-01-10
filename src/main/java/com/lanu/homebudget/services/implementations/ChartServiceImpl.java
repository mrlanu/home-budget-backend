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

    @Autowired
    private TransactionRepository transactionRepository;

    public List<YearMonthSum> getSumsOfIncomesExpensesForYearByMonth(Long budgetId) {

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime dateEnd = today.withDayOfMonth(1).plusMonths(1).minusDays(1);
        LocalDateTime dateStart = dateEnd.minusYears(1).plusDays(1);

        List<YearMonthSum> result = new ArrayList<>();
        YearMonthSum resultIncomes = new YearMonthSum(new ArrayList<>(), new ArrayList<>());
        YearMonthSum resultExspenses = new YearMonthSum(new ArrayList<>(), new ArrayList<>());

        List<Transaction> transactionList = transactionRepository
                .findAllByBudget_IdAndDateBetween(budgetId, dateStart, dateEnd);

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

        YearMonth monthShouldBe = YearMonth.now().minusYears(1).plusMonths(1);

        for (int i = 0; i < 12; i++){
            YearMonth monthPresentInArray = yearMonthSum.getDate().get(i);

            if (monthPresentInArray.equals(monthShouldBe)){
                monthShouldBe = monthShouldBe.plusMonths(1);
            } else {
                yearMonthSum.getDate().add(i, monthShouldBe);
                yearMonthSum.getSum().add(i, 0.0);
                monthShouldBe = monthShouldBe.plusMonths(1);
            }
        }
        return yearMonthSum;
    }

    public YearMonthSum getSumsByCategoryAndMonth(Long transactionId) {

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime dateEnd = today.withDayOfMonth(1).plusMonths(1).minusDays(1);
        LocalDateTime dateStart = dateEnd.minusYears(1).plusDays(1);

        YearMonthSum result = new YearMonthSum(new ArrayList<>(), new ArrayList<>());

        List<Transaction> transactionList = transactionRepository
                .findAllByCategory_IdAndDateBetween(transactionId, dateStart, dateEnd);

        Map<YearMonth, Double> yearMonthDoubleMap = transactionList.stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors
                        .groupingBy(e -> YearMonth.of(e.getDate().getYear(), e.getDate().getMonth().getValue()),
                                Collectors.summingDouble(Transaction::getAmount)));

        yearMonthDoubleMap.forEach((k, v) -> {
            result.getDate().add(k);
            result.getSum().add(v < 0 ? v * -1 : v);
        });

        return checkGapsInResultArray(result);
    }
}
