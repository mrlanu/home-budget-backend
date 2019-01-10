package com.lanu.homebudget.services;

import com.lanu.homebudget.security.User;
import com.lanu.homebudget.views.TransactionView;

import java.util.Date;
import java.util.List;

public interface TransactionViewService {
    List<TransactionView> mappingTransactionsAndTransfersToTransactionView(Long budgetId, Date date);
}
