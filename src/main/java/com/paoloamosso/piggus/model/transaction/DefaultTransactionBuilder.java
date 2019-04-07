package com.paoloamosso.piggus.model.transaction;

import com.paoloamosso.piggus.model.User;
import com.paoloamosso.piggus.util.Paths;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Default transaction builder integrated with factory of default transactionType
 */
@Slf4j
public class DefaultTransactionBuilder<T extends TransactionTypes> implements TransactionBuilder {

    // ==== fields ====
    protected T transactionType;
    private String title;
    private String description;
    private LocalDate localDate;
    private User user;
    private double moneyTransaction = 0;
    private String category;
    private Boolean isRecurrent = false;
    private int recurrentMultiplier = 0;
    private int recurrentFactor = 0;
    private Boolean isArchived = false;

    private String defaultPath;
    private Map<String,Object> inputMap;

    // ==== constructor ====
    public DefaultTransactionBuilder () {}

    // ==== builder =====
    @Override
    public TransactionBuilder create(final Object transactionTypes) {
        this.transactionType = (T) transactionTypes;
        this.defaultPath = Paths.TRANSACTION_DEFAULT_PATH.getPath();
        inputMap = new HashMap<>();

        // Setting defauls values
        inputMap.put("transactionType",this.transactionType);
        inputMap.put("moneyTransaction",this.moneyTransaction);
        inputMap.put("isRecurrent",this.isRecurrent);
        inputMap.put("recurrentMultiplier",this.recurrentMultiplier);
        inputMap.put("recurrentFactor",this.recurrentFactor);
        inputMap.put("isArchived",this.isArchived);
        return this;
    }

    @Override
    public TransactionBuilder title(final String title) {
        this.title = title;
        inputMap.put("title",this.title);
        return this;
    }

    @Override
    public TransactionBuilder description(final String description) {
        this.description = description;
        inputMap.put("description",this.description);
        return this;
    }

    @Override
    public TransactionBuilder localDate(final LocalDate localDate) {
        this.localDate = localDate;
        inputMap.put("localDate",this.localDate);
        return this;
    }

    @Override
    public TransactionBuilder user(final User user) {
        this.user = user;
        inputMap.put("user",this.user);
        return this;
    }

    @Override
    public TransactionBuilder category(final String category) {
        this.category = category;
        inputMap.put("category",this.category);
        return this;
    }

    @Override
    public TransactionBuilder moneyTransaction(final double moneyTransaction) {
        this.moneyTransaction = moneyTransaction;
        inputMap.replace("moneyTransaction",this.moneyTransaction);
        return this;
    }

    @Override
    public TransactionBuilder isRecurrent(final boolean isRecurrent) {
        this.isRecurrent = isRecurrent;
        inputMap.replace("isRecurrent",this.isRecurrent);
        return this;
    }

    @Override
    public TransactionBuilder recurrentMultiplier(final int recurrentMultiplier) {
        this.recurrentMultiplier = recurrentMultiplier;
        inputMap.replace("recurrentMultiplier",this.recurrentMultiplier);
        return this;
    }

    @Override
    public TransactionBuilder recurrentFactor(final int recurrentFactor) {
        this.recurrentFactor = recurrentFactor;
        inputMap.replace("recurrentFactor",this.recurrentFactor);
        return this;
    }

    @Override
    public TransactionBuilder isArchived(final boolean isArchived) {
        this.isArchived = isArchived;
        inputMap.replace("isArchived",this.isArchived);
        return this;
    }

    @Override
    public Transaction build() {
        Transaction transaction = null;

        try {
            Class transactionClass = Class.forName(defaultPath + transactionType.getClassName());
            transaction = (Transaction) transactionClass.getDeclaredConstructor(HashMap.class).newInstance(inputMap);
        } catch (ClassNotFoundException cnfe) {
            log.error("Builder not able to build for class not found. Error {}",cnfe.getMessage());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException re) {
            log.error("Builder not able to build for reflection exception. Error {}", re.getMessage());
        }

        return transaction;
    }
}
