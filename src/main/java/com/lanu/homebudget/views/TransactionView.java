package com.lanu.homebudget.views;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionView {

    private Long id;
    private LocalDateTime date;
    private String type;
    private String description;
    private double amount;
    private String category;
    private String subCategory;
    private String account;
    private String accountType;
}
