package com.paoloamosso.piggus.service;

import com.paoloamosso.piggus.model.transaction.Transaction;
import com.paoloamosso.piggus.model.transportation.TransactionRequest;

import java.util.List;

public interface TransactionService {

    void addTransaction(Transaction transaction);
    List<Transaction> listTransactions();
    TransactionRequest buildTransactionRequest(Transaction transaction);
}
