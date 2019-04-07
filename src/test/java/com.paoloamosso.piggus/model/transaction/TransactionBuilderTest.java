package com.paoloamosso.piggus.model.transaction;

import com.paoloamosso.piggus.util.EnumUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static com.paoloamosso.piggus.model.transaction.DefaultTransactionTypes.DEFAULT_EXPENSE;

@Slf4j
public class TransactionBuilderTest {

    @Test
    public void test(){
        EnumUtils<DefaultTransactionTypes> enumUtils = new EnumUtils(DefaultTransactionTypes.class);
        TransactionBuilder<DefaultTransactionTypes> transactionBuilder = new DefaultTransactionBuilder<>();
        Transaction transaction = transactionBuilder
                .create(enumUtils.stringToEnum("Default_Expense"))
                .title("Ciao")
                .build();

        Transaction newTransaction = transactionBuilder
                .create(enumUtils.stringToEnum("Default_Expense"))
                .isArchived(true)
                .build();
        log.info(transaction.toString());
        log.info(newTransaction.toString());
    }
}
