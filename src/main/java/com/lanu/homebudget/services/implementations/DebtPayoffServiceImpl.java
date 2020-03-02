package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Debt;
import com.lanu.homebudget.views.DebtStrategyReport;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DebtPayoffServiceImpl {
    private static int extraPayment = 100;
    private static List<Debt> debtsList = Arrays.asList(
            new Debt("test1", "City VISA", BigDecimal.valueOf(300), BigDecimal.valueOf(1000),
                    15, 25, LocalDate.now().plusMonths(1), new ArrayList<>()),
            new Debt("test2", "CHASE VISA", BigDecimal.valueOf(700), BigDecimal.valueOf(2000),
                    14, 25, LocalDate.now().plusMonths(1), new ArrayList<>())/*,
            new Debt("test3", "Car loan", BigDecimal.valueOf(10000), BigDecimal.valueOf(1000),
                    2.5, 25, LocalDate.now().plusMonths(1), new ArrayList<>()),
            new Debt("test4", "Bank of America VISA", BigDecimal.valueOf(3000), BigDecimal.valueOf(500),
                    13, 25, LocalDate.now().plusMonths(1), new ArrayList<>())*/
    );

    public static void main(String[] args) {
        List<Debt> debts = sortDebts(debtsList, "apr");
        //debts.forEach(System.out::println);
        //System.out.println(isCompletedDebt(debts));
        testMethod(debts);
    }

    private static List<Debt> sortDebts(List<Debt> debtsList, String strategy){
        return strategy.equals("apr") ?
                debtsList.stream()
                .sorted((debt1, debt2) -> {
                    if (debt1.getApr() > debt2.getApr()) return -1;
                    if (debt1.getApr() < debt2.getApr()) return 1;
                    if (debt1.getApr() == debt2.getApr()) {
                        if (debt1.getCurrentBalance().compareTo(debt2.getCurrentBalance()) > 0) {
                            return 1;
                        } else return -1;
                    }
                    return 0;
                })
                .collect(Collectors.toList()) :
                debtsList.stream()
                        .sorted((debt1, debt2) -> {
                            if (debt1.getCurrentBalance().compareTo(debt2.getCurrentBalance()) > 0) return 1;
                            if (debt1.getCurrentBalance().compareTo(debt2.getCurrentBalance()) < 0) return -1;
                            if (debt1.getCurrentBalance().compareTo(debt2.getCurrentBalance()) == 0) {
                                if (debt1.getApr() > debt2.getApr()) {
                                    return -1;
                                } else return 1;
                            }
                            return 0;
                        })
                        .collect(Collectors.toList());
    }

    private static boolean isCompletedDebt(List<Debt> debtsList){
        int allExtra = extraPayment + debtsList
                .stream()
                .filter(debt -> debt.getCurrentBalance().compareTo(BigDecimal.ZERO) == 0)
                .mapToInt(Debt::getMinimumPayment)
                .sum();

        Debt debtForExtra = debtsList.stream()
                .filter(d -> d.getCurrentBalance().doubleValue() > 0)
                .findFirst()
                .orElseThrow(InvalidParameterException::new);

        if (debtForExtra.getCurrentBalance()
                .compareTo(BigDecimal.valueOf(allExtra).add(BigDecimal.valueOf(debtForExtra.getMinimumPayment()))) <= 0){
            return true;
        }

        for (int i = 0; i < debtsList.size(); i++) {
            Debt debt = debtsList.get(i);

            if (debt.getCurrentBalance().compareTo(BigDecimal.ZERO) > 0 &&
                    debt.getCurrentBalance().compareTo(BigDecimal.valueOf(debt.getMinimumPayment())) <= 0){
                return true;
            }
        }
        return false;
    }

    private static Debt makeMinPayment(Debt debt){
        debt.setCurrentBalance(debt.getCurrentBalance().subtract(BigDecimal.valueOf(debt.getMinimumPayment())));
        return debt;
    }

    private static void testMethod(List<Debt> debts) {

        int duration = 0;
        List<DebtStrategyReport> debtStrategyReports = new ArrayList<>();
        DebtStrategyReport report = new DebtStrategyReport();

        debts.forEach(System.out::println);
        System.out.println("/n");

        // 1. check if there is any balance to pay
        while (debts.stream().mapToDouble(debt -> debt.getCurrentBalance().doubleValue()).sum() > 0) {
            BigDecimal extraPaym = BigDecimal.valueOf(extraPayment);
            boolean isFullPayedDebt = isCompletedDebt(debts);

            // check whether is there full payed balance or not
            if (isFullPayedDebt){
                if (duration > 0){
                    report.setDuration(duration);
                    // add all minPayments to the report except first one (which is extra payment)
                    for (int i = 1; i < debts.size(); i++) {
                        report.addMinPayment(debts.get(i).getName());
                    }

                    debtStrategyReports.add(report);
                }

                // reset the report
                report = new DebtStrategyReport();
                report.setDuration(1);
            }

            // 2. make minimum payment to all debts
            for (int i = 0; i < debts.size(); i++){
                Debt debt = debts.get(i);

                // if there in the list exist any paid debt then add its minimum payment to extraPaym
                if (debt.getCurrentBalance().doubleValue() <= 0){
                    extraPaym = extraPaym.add(BigDecimal.valueOf(debt.getMinimumPayment()));
                    continue;
                }

                debt.setCurrentBalance(debt.getCurrentBalance().subtract(BigDecimal.valueOf(debt.getMinimumPayment())));

                // check if the balance is paid
                if (debt.getCurrentBalance().doubleValue() <= 0) {

                    // if yes, add leftover to extraPayment
                    extraPaym = debt.getCurrentBalance().multiply(BigDecimal.valueOf(-1)).add(extraPaym);
                    debt.setCurrentBalance(BigDecimal.ZERO);
                }
            }

            // 3. make extra payment
            while (extraPaym.doubleValue() > 0) {

                // find first debt with currentBalance > 0
                Debt debt = debts.stream()
                        .filter(d -> d.getCurrentBalance().doubleValue() > 0)
                        .findFirst()
                        .orElseGet(Debt::new);

                if (debt.getMinimumPayment() == 0)break;

                debt.setCurrentBalance(debt.getCurrentBalance().subtract(extraPaym));
                extraPaym = BigDecimal.ZERO;
                report.addExtraPayment(debt.getName());

                // check if the balance is paid
                if (debt.getCurrentBalance().doubleValue() <= 0) {

                    // if yes, add leftover to extraPayment
                    extraPaym = debt.getCurrentBalance().multiply(BigDecimal.valueOf(-1)).add(extraPaym);
                    debt.setCurrentBalance(BigDecimal.ZERO);
                }
            }

            if (isFullPayedDebt){
                for (Debt d: debts) {
                    if (d.getCurrentBalance().compareTo(BigDecimal.ZERO) > 0){
                        report.addMinPayment(d.getName());
                    }
                }
                debtStrategyReports.add(report);
                report = new DebtStrategyReport();
                duration = 0;
            }
            else duration++;

            debts.forEach(System.out::println);
            System.out.println("\n");
        }

        debtStrategyReports.forEach(System.out::println);
    }
}
