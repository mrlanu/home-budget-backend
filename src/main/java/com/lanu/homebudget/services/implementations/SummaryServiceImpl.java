package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Category;
import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.repositories.TransactionRepository;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.services.SummaryService;
import com.lanu.homebudget.views.Group;
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

    @Override
    public List<Group> getSummaryByCategory(User user, Date date, Transaction.TransactionType type) {

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDateStart = localDate.withDayOfMonth(1);
        LocalDate localDateEnd = localDate.plusMonths(1).withDayOfMonth(1).minusDays(1);

        List<Group> result = new ArrayList<>();

        List<Transaction> transactionList = transactionRepository
                .findAllByUserAndDateBetweenAndType(user, localDateStart, localDateEnd, type);

        Map<Category, List<Transaction>> groupedByCategory = transactionList
                .stream()
                .collect(Collectors.groupingBy(Transaction::getCategory));

        groupedByCategory.forEach((key,value) -> result.add(new Group(
                key.getId(),
                key.getName(),
                value
                        .stream()
                        .mapToDouble(Transaction::getAmount)
                        .sum(),
                value)));
        return result;
    }
}
