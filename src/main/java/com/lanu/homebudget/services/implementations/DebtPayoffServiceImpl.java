package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Debt;
import com.lanu.homebudget.repositories.BudgetRepository;
import com.lanu.homebudget.repositories.DebtRepository;
import com.lanu.homebudget.services.DebtPayoffService;
import com.lanu.homebudget.views.DebtReportItem;
import com.lanu.homebudget.views.DebtStrategyReport;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DebtPayoffServiceImpl implements DebtPayoffService {

    private DebtRepository debtRepository;
    private BudgetRepository budgetRepository;

    public DebtPayoffServiceImpl(DebtRepository debtRepository, BudgetRepository budgetRepository) {
        this.debtRepository = debtRepository;
        this.budgetRepository = budgetRepository;
    }

    @Override
    public Debt createDebt(Long budgetId, Debt debt){
        debt.setBudget(budgetRepository.findById(budgetId).get());
        return debtRepository.save(debt);
    }

    @Override
    public List<Debt> getAllDebtsByBudgetId(Long budgetId){
        return debtRepository.findAllByBudget_Id(budgetId);
    }

    @Override
    public List<DebtStrategyReport> countDebtsPayOffStrategy(Long budgetId, double extra) {

        int duration = 0;
        List<DebtStrategyReport> debtStrategyReports = new ArrayList<>();
        DebtStrategyReport report = new DebtStrategyReport();
        List<Debt> debts = debtRepository.findAllByBudget_Id(budgetId);
        List<Debt> sortDebts = sortDebts(debts, "avalanche");

        // 1. check if there is any balance to pay
        while (sortDebts.stream().mapToDouble(Debt::getCurrentBalance).sum() > 0) {

            double extraPayment = extra;
            boolean isFullPayedDebt = isCompletedDebt(sortDebts, extraPayment);
            double tempCurrentBalance;

            // check whether is there full payed balance or not
            if (isFullPayedDebt){
                if (duration > 0){
                    report.setDuration(duration);
                    // add all minPayments to the report except first one (which is extra payment)
                    for (int i = 1; i < sortDebts.size(); i++) {
                        Debt d = sortDebts.get(i);
                        if (d.getCurrentBalance() > 0) {
                            report.addMinPayment(new DebtReportItem(d.getName(), d.getMinimumPayment(), false));
                        }
                    }
                    debtStrategyReports.add(report);
                }

                // reset the report
                report = new DebtStrategyReport();
                report.setDuration(1);
            }

            // 2. make minimum payment to all debts
            for (Debt debt : sortDebts) {
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
                    report.addExtraPayment(new DebtReportItem(debt.getName(), tempCurrentBalance, true));
                    extraPayment = -debt.getCurrentBalance() + extraPayment;
                    debt.setCurrentBalance(0);
                }
            }

            // 3. make extra payment
            while (extraPayment > 0) {
                // find first debt with currentBalance > 0
                Debt debt = sortDebts.stream()
                        .filter(d -> d.getCurrentBalance() > 0)
                        .findFirst()
                        .orElseGet(Debt::new);
                // for the last debt in the list of Debts
                if (debt.getMinimumPayment() == 0)break;

                tempCurrentBalance = debt.getCurrentBalance();
                debt.setCurrentBalance(debt.getCurrentBalance() - extraPayment);

                // check if the balance is paid
                if (debt.getCurrentBalance() <= 0) {
                    report.addExtraPayment(new DebtReportItem(debt.getName(), tempCurrentBalance + debt.getMinimumPayment(), true));
                    extraPayment = 0;
                    // if yes, add leftover to extraPayment
                    extraPayment = -debt.getCurrentBalance() + extraPayment;
                    debt.setCurrentBalance(0);
                    continue;
                }
                report.addExtraPayment(new DebtReportItem(debt.getName(), debt.getMinimumPayment() + extraPayment, false));
                extraPayment = 0;
            }

            if (isFullPayedDebt){
                for (Debt d: sortDebts) {
                    if (d.getCurrentBalance() > 0){
                        report.addMinPayment(new DebtReportItem(d.getName(), d.getMinimumPayment(), false));
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
        return debtStrategyReports;
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
}
