package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Account;
import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.entities.Transfer;
import com.lanu.homebudget.exceptions.ResourceNotFoundException;
import com.lanu.homebudget.repositories.TransferRepository;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.services.AccountService;
import com.lanu.homebudget.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

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
    public Transfer createTransfer(User user, Transfer transfer) {
        Account accFrom = accountService.findAccountById(transfer.getFromAccount().getId());
        Account accTo = accountService.findAccountById(transfer.getToAccount().getId());
        accFrom.setBalance(accFrom.getBalance() - transfer.getAmount());
        accTo.setBalance(accTo.getBalance() + transfer.getAmount());

        accountService.saveAccount(accFrom);
        accountService.saveAccount(accTo);

        transfer.setUser(user);

        return transferRepository.save(transfer);
    }

    @Override
    public Transfer editTransfer(Transfer transferRequest){
        if(!transferRepository.existsById(transferRequest.getId())) {
            throw new ResourceNotFoundException("TransferId " + transferRequest.getId() + " not found");
        }

        return transferRepository.findById(transferRequest.getId()).map(transfer -> {

                Account accountFrom = accountService.findAccountById(transfer.getFromAccount().getId());
                accountFrom.setBalance(accountFrom.getBalance() + transfer.getAmount());
                accountService.saveAccount(accountFrom);

                accountFrom = accountService.findAccountById(transferRequest.getFromAccount().getId());
                accountFrom.setBalance(accountFrom.getBalance() - transferRequest.getAmount());
                accountService.saveAccount(accountFrom);

                Account accountTo = accountService.findAccountById(transfer.getToAccount().getId());
                accountTo.setBalance(accountTo.getBalance() - transfer.getAmount());
                accountService.saveAccount(accountTo);

                accountTo = accountService.findAccountById(transferRequest.getToAccount().getId());
                accountTo.setBalance(accountTo.getBalance() + transferRequest.getAmount());
                accountService.saveAccount(accountTo);

            transfer.setFromAccount(transferRequest.getFromAccount());
            transfer.setToAccount(transferRequest.getToAccount());
            transfer.setAmount(transferRequest.getAmount());
            transfer.setDate(transferRequest.getDate());

            return transferRepository.save(transfer);
        }).orElseThrow(() -> new ResourceNotFoundException("TransferId " + transferRequest.getId() + "not found"));
    }

    @Override
    public List<Transfer> findAllByUserAndDateBetween(User user, Date date) {

        LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime localDateStart = localDate.withDayOfMonth(1);
        LocalDateTime localDateEnd = localDate.plusMonths(1).withDayOfMonth(1).minusDays(1);

        return transferRepository.findAllByUserAndDateBetween(user, localDateStart, localDateEnd);
    }
}
