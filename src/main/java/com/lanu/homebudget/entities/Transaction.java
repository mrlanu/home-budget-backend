package com.lanu.homebudget.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;
    private String type;
    private String description;
    private double amount;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
