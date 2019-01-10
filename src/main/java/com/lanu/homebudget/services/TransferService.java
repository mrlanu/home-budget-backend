package com.lanu.homebudget.services;

import com.lanu.homebudget.entities.Transfer;
import com.lanu.homebudget.security.User;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface TransferService {
    Transfer getTransferById(Long transferId);
    List<Transfer> findAllByBudgetIdAndDateBetween(Long budgetId, Date date);
    Transfer createTransfer(Long budgetId, Transfer transfer);
    Transfer editTransfer(Transfer transferRequest);
    ResponseEntity<?> deleteTransfer(Long transferId);
}
