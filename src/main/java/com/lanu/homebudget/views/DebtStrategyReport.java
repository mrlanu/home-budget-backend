package com.lanu.homebudget.views;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DebtStrategyReport {
    private int duration;
    private List<DebtReportItem> extraPayments;
    private List<DebtReportItem> minPayments;

    public DebtStrategyReport() {
        this.extraPayments = new ArrayList<>();
        this.minPayments = new ArrayList<>();
    }

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
