package com.lanu.homebudget.entities;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Payment {
    private double amount;
    private LocalDate date;
}
