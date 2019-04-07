/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * Here the transaction domain
 */

package com.paoloamosso.piggus.model.transaction;

import com.paoloamosso.piggus.converter.LocalDatePersistenceConverter;
import com.paoloamosso.piggus.model.User;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;

@Data
@Entity
@Table(name = "transaction")
public class AbstractTransaction implements Transaction {

    // == fields ==
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private int id;
    @Column(name = "transaction_type")
    private TransactionTypes transactionType;

    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "date")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate localDate;
    @ManyToOne
    private User user;
    @Column(name = "money_transaction")
    private double moneyTransaction;
    @Column(name = "category")
    private String category;

    @Column(name = "is_recurrent")
    private Boolean isRecurrent;
    @Column(name = "recurrent_factor")
    private int recurrentFactor;
    @Column(name = "recurrent_multiplier")
    private int recurrentMultiplier;

    @Column(name = "is_archived")
    private Boolean isArchived = false;

    // ==== constructor ====
    public AbstractTransaction() {}

    /**
     * This constructor is used in the TransactionBuilder to generate a new Transaction instance,
     * a HashMap<String,Object> is used to pass the parameters so that the builder does not need public getters
     *
     * @param objectHashMap
     */
    AbstractTransaction(HashMap<String, Object> objectHashMap) {
        this.transactionType = (TransactionTypes) objectHashMap.get("transactionType");
        this.title = (String) objectHashMap.get("title");
        this.description = (String) objectHashMap.get("description");
        this.localDate = (LocalDate) objectHashMap.get("localDate");
        this.user = (User) objectHashMap.get("user");
        this.moneyTransaction = (double) objectHashMap.get("moneyTransaction");
        this.category = (String) objectHashMap.get("category");
        this.isRecurrent = (Boolean) objectHashMap.get("isRecurrent");
        this.recurrentFactor = (int) objectHashMap.get("recurrentFactor");
        this.recurrentMultiplier = (int) objectHashMap.get("recurrentMultiplier");
        this.isArchived = (Boolean) objectHashMap.get("isArchived");
    }

    // ==== methods ====
}
