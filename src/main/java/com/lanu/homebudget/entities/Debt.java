package com.lanu.homebudget.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Debt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double startBalance;
    private double currentBalance;
    private double apr;
    private  double minimumPayment;
    private LocalDate nextPaymentDue;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "debt_id")
    private List<Payment> paymentsList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id")
    @JsonIgnore
    private Budget budget;
}

