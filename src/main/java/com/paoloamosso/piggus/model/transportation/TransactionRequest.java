package com.paoloamosso.piggus.model.transportation;

import com.paoloamosso.piggus.model.User;
import com.paoloamosso.piggus.model.transaction.Transaction;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

@Data
public abstract class TransactionRequest {

    // ==== fields ====
    private int id;
    private String transactionType;
    private String title;
    private String description;
    private LocalDate localDate;
    private BigDecimal moneyTransaction;
    private String category;
    private Boolean isRecurrent;
    private int recurrentFactor;
    private int recurrentMultiplier;

    // ==== constructor ====
    public TransactionRequest() {}

    public TransactionRequest (HashMap<String, Object> objectHashMap) {
        this.id = (int) objectHashMap.get("id");
        this.title = (String) objectHashMap.get("title");
        this.description = (String) objectHashMap.get("description");
        this.localDate = (LocalDate) objectHashMap.get("localDate");
        this.moneyTransaction = (BigDecimal) objectHashMap.get("moneyTransaction");
        this.category = (String) objectHashMap.get("category");
        this.isRecurrent = (Boolean) objectHashMap.get("isRecurrent");
        this.recurrentFactor = (int) objectHashMap.get("recurrentFactor");
        this.recurrentMultiplier = (int) objectHashMap.get("recurrentMultiplier");
    }
}
