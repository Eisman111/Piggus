/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * Here the transaction domain
 */

package com.paoloamosso.piggus.model;

import com.paoloamosso.piggus.converter.LocalDatePersistenceConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

// With Data with add getter and setter to each variable
@Data
// With equals with add a equal functionality to object by matching the id
@EqualsAndHashCode(of = "id")
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "transaction")
public class Transaction {

    // == fields ==
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private int id;
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
    @Column(name = "transaction_type")
    private String transactionType;
    @Column(name = "is_recurrent")
    private Boolean isRecurrent;
    @Column(name = "recurrent_factor")
    private int recurrentFactor;
    @Column(name = "is_archived")
    private Boolean isArchived = false;

    // == constructors ==
    public Transaction () {}

    public Transaction(Transaction transaction) {
        this.title = transaction.title;
        this.description = transaction.description;
        this.localDate = transaction.localDate;
        this.user = transaction.user;
        this.moneyTransaction = transaction.moneyTransaction;
        this.transactionType = transaction.transactionType;
        this.isRecurrent = transaction.isRecurrent;
        this.recurrentFactor = transaction.recurrentFactor;
        this.isArchived = transaction.isArchived;
    }
}
