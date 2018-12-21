package com.lanu.homebudget.views;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.YearMonth;
import java.util.List;

@Data
@AllArgsConstructor
public class YearMonthSum {
    private List<YearMonth> date;
    private List<Double> sum;
}
