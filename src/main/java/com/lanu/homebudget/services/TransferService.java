package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Transfer;
import com.lanu.homebudget.security.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface TransferService {
    Transfer getTransferById(Long transferId);
    List<Transfer> findAllByUserAndDateBetween(User user, Date date);
    Transfer createTransfer(User user, LocalDateTime date, Long fromAccId, Long toAccId, double amount);
}