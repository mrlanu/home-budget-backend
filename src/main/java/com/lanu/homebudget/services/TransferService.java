package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Transfer;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.views.TransactionView;

import java.util.Date;
import java.util.List;

public interface TransferService {
    Transfer getTransferById(Long transferId);
    List<TransactionView> findAllByUserAndDateBetween(User user, Date date);
    Transfer createTransfer(User user, Long fromAccId, Long toAccId, double amount);
}
