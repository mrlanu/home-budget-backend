package com.lanu.homebudget.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtPayoffStrategy {
    private int totalDuration;
    private BigDecimal totalInterest;
    private LocalDate debtFreeDate;
    private List<DebtStrategyReport> reports;
}
