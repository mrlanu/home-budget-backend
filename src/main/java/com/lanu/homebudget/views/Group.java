package com.lanu.homebudget.views;

import com.lanu.homebudget.entities.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Group {
    private Long id;
    private String name;
    private double spent;
    private List<Transaction> transactionList;
}
