package com.lanu.homebudget.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Debt {
    private String publicId;
    private String name;
    private BigDecimal startBalance;
    private BigDecimal currentBalance;
    private double apr;
    private  int minimumPayment;
    private LocalDate nextPaymentDue;
    private List<Payment> paymentsList;
}

