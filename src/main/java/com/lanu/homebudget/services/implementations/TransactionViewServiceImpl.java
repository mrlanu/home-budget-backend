package com.lanu.homebudget.services.implementations;

import com.lanu.homebudget.entities.Transaction;
import com.lanu.homebudget.entities.Transfer;
import com.lanu.homebudget.security.User;
import com.lanu.homebudget.services.TransactionService;
import com.lanu.homebudget.services.TransactionViewService;
import com.lanu.homebudget.services.TransferService;
import com.lanu.homebudget.views.TransactionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionViewServiceImpl implements TransactionViewService {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransferService transferService;

    public List<TransactionView> mappingTransactionsAndTransfersToTransactionView(User user, Date date) {

        List<TransactionView> result = new ArrayList<>();
        List<Transaction> transactionList = transactionService.findAllByUserAndDateBetween(user, date);
        List<Transfer> transferList = transferService.findAllByUserAndDateBetween(user, date);

        transactionList.forEach(transaction ->
                result.add(new TransactionView(
                        transaction.getId(),
                        transaction.getDate(),
                        transaction.getType().toString(),
                        transaction.getDescription(),
                        transaction.getAmount(),
                        transaction.getCategory().getName(),
                        transaction.getSubCategory().getName(),
                        transaction.getAccount().getName(),
                        transaction.getAccount().getType()))
        );

        transferList.forEach(transfer -> {
                result.add(new TransactionView(
                        transfer.getId(),
                        transfer.getDate(),
                        Transaction.TransactionType.TRANSFER.toString(),
                        "to " + transfer.getToAccount().getName(),
                        -transfer.getAmount(),
                        "Transfer",
                        "Out",
                        transfer.getFromAccount().getName(),
                        transfer.getFromAccount().getType()));
                result.add(new TransactionView(
                        transfer.getId(),
                        transfer.getDate(),
                        Transaction.TransactionType.TRANSFER.toString(),
                        "from " + transfer.getFromAccount().getName(),
                        transfer.getAmount(),
                        "Transfer",
                        "In",
                        transfer.getToAccount().getName(),
                        transfer.getToAccount().getType()));
        });

        return result;
    }
}

