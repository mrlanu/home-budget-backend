package com.lanu.homebudget.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lanu.homebudget.security.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@Temporal(TemporalType.TIMESTAMP)
    @CreatedDate*/
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    public enum TransactionType{
        EXPENSE, INCOME
    }

    private String description;
    private double amount;

    @OneToOne
    private Category category;

    @OneToOne
    private SubCategory subCategory;

    @OneToOne
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
