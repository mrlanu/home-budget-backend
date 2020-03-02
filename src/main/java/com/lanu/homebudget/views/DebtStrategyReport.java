package com.lanu.homebudget.views;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DebtStrategyReport {
    private int duration;
    private List<DebtReportItem> extraPayments;
    private List<DebtReportItem> minPayments;

    public void addExtraPayment(DebtReportItem extra){
        if (extraPayments == null){
            extraPayments = new ArrayList<>();
        }
        if (!extraPayments.contains(extra)){
            extraPayments.add(extra);
        }
    }

    public void addMinPayment(DebtReportItem min){
        if (minPayments == null){
            minPayments = new ArrayList<>();
        }
        if (!minPayments.contains(min) && !extraPayments.contains(min)){
            minPayments.add(min);
        }
    }

}
