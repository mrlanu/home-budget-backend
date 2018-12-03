package com.lanu.homebudget.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lanu.homebudget.security.User;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date date;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    public enum TransactionType{
        EXPENSE, INCOME
    }

    private String description;
    private double amount;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
