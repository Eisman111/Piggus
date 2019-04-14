package com.paoloamosso.piggus.model.transportation;

import com.paoloamosso.piggus.model.User;
import com.paoloamosso.piggus.model.transaction.Transaction;
import com.paoloamosso.piggus.model.transaction.TransactionBuilder;
import com.paoloamosso.piggus.util.Paths;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

@Slf4j
public class DefaultTransactionRequestBuilder<T extends TransactionRequest> implements TransactionRequestBuilder {

    // ==== fields ====
    private int id;
    private String title;
    private String description;
    private LocalDate localDate;
    private BigDecimal moneyTransaction;
    private String category;
    private Boolean isRecurrent;
    private int recurrentFactor;
    private int recurrentMultiplier;

    private HashMap<String, Object> objectHashMap;

    private T requestType;

    // ==== constructor ====
    public DefaultTransactionRequestBuilder () {}

    // ==== builder =====
    @Override
    public TransactionRequestBuilder start() {

        objectHashMap = new HashMap<>();

        return this;
    }

    @Override
    public TransactionRequestBuilder convertTransaction(Transaction val) {

        this.id = val.getId();
        objectHashMap.put("id",id);
        this.title = val.getTitle();
        objectHashMap.put("title",title);
        this.description = val.getDescription();
        objectHashMap.put("description",description);
        this.localDate = val.getLocalDate();
        objectHashMap.put("localDate",localDate);
        this.moneyTransaction = val.getMoneyTransaction();
        objectHashMap.put("moneyTransaction",moneyTransaction);
        this.category = val.getCategory();
        objectHashMap.put("category",category);
        this.isRecurrent = val.getIsRecurrent();
        objectHashMap.put("isRecurrent",isRecurrent);
        this.recurrentFactor = val.getRecurrentFactor();
        objectHashMap.put("recurrentFactor",recurrentFactor);
        this.recurrentMultiplier = val.getRecurrentMultiplier();
        objectHashMap.put("recurrentMultiplier",recurrentMultiplier);

        return this;
    }

    @Override
    public T build() {

        T transactionRequest = null;

        try {
            transactionRequest = (T) requestType.getClass().getDeclaredConstructor(HashMap.class).newInstance(this.objectHashMap);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ee) {
            log.error("Unable to build the TransactionRequest from the transaction id: " + id + ", error: " + ee.getMessage());
        }
        return transactionRequest;
    }
}
