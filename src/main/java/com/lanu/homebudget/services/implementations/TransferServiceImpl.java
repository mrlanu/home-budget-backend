package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.*;
import com.lanu.homebudget.exceptions.ResourceNotFoundException;
import com.lanu.homebudget.repositories.TransferRepository;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.services.AccountService;
import com.lanu.homebudget.services.TransferService;
import com.lanu.homebudget.views.TransactionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferServiceImpl implements TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public Transfer getTransferById(Long transferId) {
        return transferRepository.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException("TransferId " + transferId + " not found"));
    }

    @Override
    public Transfer createTransfer(User user, LocalDateTime date, Long fromAccId, Long toAccId, double amount) {
        Account accFrom = accountService.findAccountById(fromAccId);
        Account accTo = accountService.findAccountById(toAccId);
        accFrom.setBalance(accFrom.getBalance() - amount);
        accTo.setBalance(accTo.getBalance() + amount);

        accountService.saveAccount(accFrom);
        accountService.saveAccount(accTo);

        Transfer transfer =
                new Transfer(null, date, accFrom, accTo, amount, user);

        return transferRepository.save(transfer);
    }

    @Override
    public List<Transfer> findAllByUserAndDateBetween(User user, Date date) {

        LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime localDateStart = localDate.withDayOfMonth(1);
        LocalDateTime localDateEnd = localDate.plusMonths(1).withDayOfMonth(1).minusDays(1);

        return transferRepository.findAllByUserAndDateBetween(user, localDateStart, localDateEnd);
    }
}
