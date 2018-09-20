/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * Here the expense domain
 */

package com.paoloamosso.piggus.model;

import com.paoloamosso.piggus.converter.LocalDatePersistenceConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

// With Data with add getter and setter to each variable
@Data
// With equals with add a equal functionality to object by matching the id
@EqualsAndHashCode(of = "id")
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "expense")
public class Expense {

    // == fields ==
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private int id;
    @NotNull
    @Column(name = "title")
    private String title;
    @NotNull
    @Column(name = "cost")
    private double cost;
    @Column(name = "description")
    private String description;
    @NotNull
    @Column(name = "date")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate localDate;
    @NotNull
    @Column(name = "fixed_cost")
    private boolean fixedCost;
    @NotNull
    @Column(name = "number_months")
    private int numberMonths;
    @ManyToOne
    @NotNull
    private User user;
    @Column(name = "expense_type")
    private String expenseType;
}
