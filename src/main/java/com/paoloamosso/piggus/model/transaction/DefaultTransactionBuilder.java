package com.paoloamosso.piggus.model.transaction;

import com.paoloamosso.piggus.model.User;
import com.paoloamosso.piggus.model.transportation.DefaultTransactionRequest;
import com.paoloamosso.piggus.model.transportation.TransactionRequest;
import com.paoloamosso.piggus.util.Paths;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Default transaction builder integrated with factory of default category
 */
@Slf4j
public class DefaultTransactionBuilder<T extends TransactionTypes> implements TransactionBuilder {

    // ==== fields ====
    private int id;
    protected T transactionType;
    private String title;
    private String description;
    private LocalDate localDate;
    private User user;
    private BigDecimal moneyTransaction = new BigDecimal("0");
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
    public TransactionBuilder start(final Object transactionTypes) {

        try {
            this.transactionType = (T) transactionTypes;
        } catch (ClassCastException cce) {
            log.error("The builder was created with the wrong input object, not the same as the type parameter");
            throw new ClassCastException();
        }

        this.defaultPath = Paths.TRANSACTION_DEFAULT_PATH.getPath();
        inputMap = new HashMap<>();

        // Setting defauls values
        inputMap.put("moneyTransaction",new BigDecimal("0"));
        inputMap.put("isRecurrent",false);
        inputMap.put("recurrentMultiplier",0);
        inputMap.put("recurrentFactor",0);
        inputMap.put("isArchived",false);
        return this;
    }

    @Override
    public TransactionBuilder convertTransportationRequest(TransactionRequest val) {

        this.id(val.getId());
        this.title(val.getTitle());
        this.description((val.getDescription()));
        this.localDate(val.getLocalDate());
        this.moneyTransaction(String.valueOf(val.getMoneyTransaction()));
        this.category(val.getCategory());
        this.isRecurrent(val.getIsRecurrent());
        this.recurrentFactor(val.getRecurrentFactor());
        this.recurrentMultiplier(val.getRecurrentMultiplier());

        return this;
    }

    @Override
    public TransactionBuilder id(int val) {

        this.id = id;
        inputMap.put("id",this.id);
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
    public TransactionBuilder moneyTransaction(final String moneyTransaction) {

        // New BigDecimal(String) is not null safe
        this.moneyTransaction = new BigDecimal(moneyTransaction == null ? "0" : moneyTransaction);
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
            inputMap.clear();
        } catch (ClassNotFoundException cnfe) {
            log.error("Builder not able to build for class not found. Error {}",cnfe.getMessage());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException re) {
            log.error("Builder not able to build for reflection exception. Error {}", re.getMessage());
        }

        return transaction;
    }
}
