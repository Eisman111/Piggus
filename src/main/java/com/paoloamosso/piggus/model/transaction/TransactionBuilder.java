package com.paoloamosso.piggus.model.transaction;

import com.paoloamosso.piggus.model.User;

import java.time.LocalDate;

public interface TransactionBuilder<T extends TransactionTypes> {
    TransactionBuilder create(Object val);
    TransactionBuilder title(String val);
    TransactionBuilder description(String val);
    TransactionBuilder localDate(LocalDate val);
    TransactionBuilder user(User val);
    TransactionBuilder moneyTransaction(double val);
    TransactionBuilder category(String val);
    TransactionBuilder isRecurrent(boolean val);
    TransactionBuilder recurrentMultiplier(int val);
    TransactionBuilder recurrentFactor(int val);
    TransactionBuilder isArchived(boolean val);
    Transaction build();
}
