/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * Here the user domain
 */

package com.paoloamosso.piggus.model;

import com.paoloamosso.piggus.converter.LocalDatePersistenceConverter;
import com.paoloamosso.piggus.model.transaction.Transaction;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
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
    @Column(name = "email")
    @NotEmpty(message="*Please provide an email")
    private String email;
    @Column(name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;
    @Column(name = "active")
    private int active;
    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

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

    @Column(name = "total_saving")
    private Double totalSavings;
    @Column(name = "monthly_saving")
    private Double monthlySaving;
    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;
    @ElementCollection
    @CollectionTable(name = "categories", joinColumns = @JoinColumn(name = "user_id"))
    private Set<String> category;

    @OneToMany(mappedBy = "user")
    private List<Deadline> deadlines;
}
