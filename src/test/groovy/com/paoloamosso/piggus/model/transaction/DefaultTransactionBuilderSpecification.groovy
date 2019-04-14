package com.paoloamosso.piggus.model.transaction

import com.paoloamosso.piggus.util.EnumUtils
import spock.lang.Specification

import static com.paoloamosso.piggus.model.transaction.DefaultTransactionTypes.DEFAULT_EXPENSE

class DefaultTransactionBuilderSpecification extends Specification {

    def "Should be able to build default transaction"() {
        given:
            EnumUtils<DefaultTransactionTypes> enumUtils = new EnumUtils(DefaultTransactionTypes.class)
            TransactionBuilder<DefaultTransactionTypes> transactionBuilder = new DefaultTransactionBuilder<>()

        when:
            Transaction transaction = (DefaultExpense) transactionBuilder
                    .start(enumUtils.stringToEnum("Default_Expense"))
                    .title("Ciao")
                    .moneyTransaction("103.42")
                    .build()

            Transaction newTransaction = transactionBuilder
                    .start(DEFAULT_EXPENSE)
                    .isArchived(true)
                    .moneyTransaction(null)
                    .build()
        then:
            transaction.getClass() == DefaultExpense
            transaction.getTitle() == "Ciao"
    }
}
