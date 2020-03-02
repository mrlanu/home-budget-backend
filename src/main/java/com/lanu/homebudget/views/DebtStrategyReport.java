package com.lanu.homebudget.views;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DebtStrategyReport {
    private int duration;
    private List<String> extraPayments;
    private List<String> minPayments;

    public void addExtraPayment(String extra){
        if (extraPayments == null){
            extraPayments = new ArrayList<>();
        }
        if (!extraPayments.contains(extra)){
            extraPayments.add(extra);
        }
    }

    public void addMinPayment(String min){
        if (minPayments == null){
            minPayments = new ArrayList<>();
        }
        if (!minPayments.contains(min)){
            minPayments.add(min);
        }
    }
}
