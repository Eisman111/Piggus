/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * Here the user domain
 */

package com.paoloamosso.piggus.model;

import com.paoloamosso.piggus.converter.LocalDatePersistenceConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "user")
public class User {

    // == fields ==
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "user_id")
    private int idUser;
    @Column(name = "user_public_identifier")
    private String userPublicIdentifier;
    @Column(name = "user_secret_identifier")
    private String userSecretIdentifier;
    @Column(name = "username")
    @NotEmpty(message="*Please provide an username")
    private String username;
    @Column(name = "email")
    @NotEmpty(message="*Please provide an email")
    private String email;
    @Column(name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;
    @Column(name = "active")
    private int active;
    @Column(name = "is_configured")
    private int isConfigured;
    @Column(name = "recovery_mode")
    private int recoveryMode;
    @Column(name = "registration_date")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate registrationDate;
    @Column(name = "last_login")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate lastLogin;
    @Column(name = "accepted_privacy")
    private boolean acceptedPrivacy = false;
    @Column(name = "accepted_marketing")
    private boolean acceptedMarketing = false;
    @Column(name = "monthly_budget")
    private Double monthlyBudget = 0.0;
    @Column(name = "monthly_saving")
    private Double monthlySaving = 0.0;
    @OneToMany(mappedBy = "user")
    private List<Expense> expenses;
    @OneToMany(mappedBy = "user")
    private List<Deadline> deadlines;
    @ElementCollection
    @CollectionTable(name = "expense_type", joinColumns = @JoinColumn(name = "user_id"))
    private Set<String> expenseType;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

}
