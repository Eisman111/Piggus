package com.paoloamosso.piggus.model.transportation;

import com.paoloamosso.piggus.model.transaction.Transaction;

public interface TransactionRequestBuilder<T extends TransactionRequest> {

    TransactionRequestBuilder start();
    TransactionRequestBuilder convertTransaction(Transaction val);
    T build();
}
