package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Debt;
import com.lanu.homebudget.views.DebtReportItem;
import com.lanu.homebudget.views.DebtStrategyReport;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DebtPayoffServiceImpl {

    private static List<Debt> debtsList = Arrays.asList(
            new Debt("test1", "City VISA", 300, 700,
                    15, 25, LocalDate.now().plusMonths(1), new ArrayList<>()),
            new Debt("test2", "CHASE VISA", 700, 550,
                    14, 25, LocalDate.now().plusMonths(1), new ArrayList<>()),
            new Debt("test3", "Car loan", 10000, 800,
                    2.5, 25, LocalDate.now().plusMonths(1), new ArrayList<>()),
            new Debt("test4", "Bank of America VISA", 3000, 700,
                    13, 25, LocalDate.now().plusMonths(1), new ArrayList<>())
    );

    public static void main(String[] args) {
        List<Debt> debts = sortDebts(debtsList, "avalanche");
        countDebtsPayOffStrategy(debts, 100);
    }

    private static List<Debt> sortDebts(List<Debt> debtsList, String strategy){
        return strategy.equals("apr") ?
                debtsList.stream()
                        .sorted((debt1, debt2) -> {
                            if (debt1.getApr() > debt2.getApr()) return -1;
                            if (debt1.getApr() < debt2.getApr()) return 1;
                            if (debt1.getApr() == debt2.getApr()) {
                                if (debt1.getCurrentBalance() > debt2.getCurrentBalance()) {
                                    return 1;
                                } else return -1;
                            }
                            return 0;
                        })
                        .collect(Collectors.toList()) :
                debtsList.stream()
                        .sorted((debt1, debt2) -> {
                            if (debt1.getCurrentBalance() > debt2.getCurrentBalance()) return 1;
                            if (debt1.getCurrentBalance() < debt2.getCurrentBalance()) return -1;
                            if (debt1.getCurrentBalance() == debt2.getCurrentBalance()) {
                                if (debt1.getApr() > debt2.getApr()) {
                                    return -1;
                                } else return 1;
                            }
                            return 0;
                        })
                        .collect(Collectors.toList());
    }

    private static boolean isCompletedDebt(List<Debt> debtsList, double extraPayment){
        double allExtra = extraPayment + debtsList
                .stream()
                .filter(debt -> debt.getCurrentBalance() == 0)
                .mapToDouble(Debt::getMinimumPayment)
                .sum();

        Debt debtForExtra = debtsList.stream()
                .filter(d -> d.getCurrentBalance() > 0)
                .findFirst()
                .orElseThrow(InvalidParameterException::new);

        if (debtForExtra.getCurrentBalance() <= allExtra + debtForExtra.getMinimumPayment()){
            return true;
        }

        for (Debt debt : debtsList) {
            if (debt.getCurrentBalance() > 0 &&
                    debt.getCurrentBalance() <= debt.getMinimumPayment()) {
                return true;
            }
        }
        return false;
    }

    private static void countDebtsPayOffStrategy(List<Debt> debts, double extra) {

        int duration = 0;
        List<DebtStrategyReport> debtStrategyReports = new ArrayList<>();
        DebtStrategyReport report = new DebtStrategyReport();

        // 1. check if there is any balance to pay
        while (debts.stream().mapToDouble(Debt::getCurrentBalance).sum() > 0) {

            double extraPayment = extra;
            boolean isFullPayedDebt = isCompletedDebt(debts, extraPayment);
            double tempCurrentBalance;

            // check whether is there full payed balance or not
            if (isFullPayedDebt){
                if (duration > 0){
                    report.setDuration(duration);
                    // add all minPayments to the report except first one (which is extra payment)
                    for (int i = 1; i < debts.size(); i++) {
                        Debt d = debts.get(i);
                        if (d.getCurrentBalance() > 0) {
                            report.addMinPayment(new DebtReportItem(d.getName(), d.getMinimumPayment()));
                        }
                    }
                    debtStrategyReports.add(report);
                }

                // reset the report
                report = new DebtStrategyReport();
                report.setDuration(1);
            }

            // 2. make minimum payment to all debts
            for (Debt debt : debts) {
                // if there in the list exist any paid debt then add its minimum payment to extraPayment
                if (debt.getCurrentBalance() <= 0) {
                    extraPayment += debt.getMinimumPayment();
                    continue;
                }

                // added temporary variable in case if there going to be payed balance
                tempCurrentBalance = debt.getCurrentBalance();
                debt.setCurrentBalance(debt.getCurrentBalance() - debt.getMinimumPayment());

                // check if the balance is paid
                if (debt.getCurrentBalance() <= 0) {
                    // if yes, add leftover to extraPayment
                    report.addExtraPayment(new DebtReportItem(debt.getName(), tempCurrentBalance));
                    extraPayment = -debt.getCurrentBalance() + extraPayment;
                    debt.setCurrentBalance(0);
                }
            }

            // 3. make extra payment
            while (extraPayment > 0) {
                // find first debt with currentBalance > 0
                Debt debt = debts.stream()
                        .filter(d -> d.getCurrentBalance() > 0)
                        .findFirst()
                        .orElseGet(Debt::new);
                // for the last debt in the list of Debts
                if (debt.getMinimumPayment() == 0)break;

                tempCurrentBalance = debt.getCurrentBalance();
                debt.setCurrentBalance(debt.getCurrentBalance() - extraPayment);

                // check if the balance is paid
                if (debt.getCurrentBalance() <= 0) {
                    report.addExtraPayment(new DebtReportItem(debt.getName(), tempCurrentBalance + debt.getMinimumPayment()));
                    extraPayment = 0;
                    // if yes, add leftover to extraPayment
                    extraPayment = -debt.getCurrentBalance() + extraPayment;
                    debt.setCurrentBalance(0);
                    continue;
                }
                report.addExtraPayment(new DebtReportItem(debt.getName(), debt.getMinimumPayment() + extraPayment));
                extraPayment = 0;
            }

            if (isFullPayedDebt){
                for (Debt d: debts) {
                    if (d.getCurrentBalance() > 0){
                        report.addMinPayment(new DebtReportItem(d.getName(), d.getMinimumPayment()));
                    }
                }
                debtStrategyReports.add(report);
                report = new DebtStrategyReport();
                duration = 0;
            }
            else duration++;
            /*debts.forEach(System.out::println);
            System.out.println("\n");*/
        }
        debtStrategyReports.forEach(System.out::println);
    }
}
