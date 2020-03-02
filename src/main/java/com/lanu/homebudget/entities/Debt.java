package com.lanu.homebudget.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Debt {
    private String publicId;
    private String name;
    private double startBalance;
    private double currentBalance;
    private double apr;
    private  double minimumPayment;
    private LocalDate nextPaymentDue;
    private List<Payment> paymentsList;
}

