package com.lanu.homebudget.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lanu.homebudget.security.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@Temporal(TemporalType.TIMESTAMP)
    @CreatedDate*/
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    public enum TransactionType{
        EXPENSE, INCOME, TRANSFER
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
    @JoinColumn(name = "budget_id")
    @JsonIgnore
    private Budget budget;
}
