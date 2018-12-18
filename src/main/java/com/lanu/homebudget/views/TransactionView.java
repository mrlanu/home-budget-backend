package com.lanu.homebudget.views;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TransactionView {

    private Long id;
    private LocalDate date;
    private String type;
    private String description;
    private double amount;
    private String category;
    private String subCategory;
    private String account;
    private String accountType;
}
