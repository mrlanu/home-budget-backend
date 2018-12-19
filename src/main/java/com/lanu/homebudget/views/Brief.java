package com.lanu.homebudget.views;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Brief {
    private double accountsTotal;
    private double totalSpent;
    private double totalIncome;
}
