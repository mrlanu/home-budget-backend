package com.lanu.homebudget.views;

import com.lanu.homebudget.entities.Account;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupAccount {

    private String name;
    private List<Account> accountList;
    private double balance;
}
