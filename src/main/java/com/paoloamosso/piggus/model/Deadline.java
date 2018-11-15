/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * Here the deadline domain
 */

package com.paoloamosso.piggus.model;

import com.paoloamosso.piggus.converter.LocalDatePersistenceConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "deadlines")
public class Deadline {

    // == fields ==
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private int id;
    @NotNull
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @NotNull
    @Column(name="date")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate localDate;
    @ManyToOne
    private User user;
    @Column(name="is_archived")
    private boolean archived = false;
    @Column(name="id_parent_deadline")
    private int idParentDeadline;

    // == Constructor ==
    public Deadline() {}

    public Deadline(Deadline deadline) {
        this.title = deadline.title;
        this.description = deadline.description;
        this.localDate = deadline.localDate;
        this.user = deadline.user;
        this.archived = deadline.archived;
    }
}
