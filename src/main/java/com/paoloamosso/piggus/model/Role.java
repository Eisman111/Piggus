/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * Here the role domain
 */

package com.paoloamosso.piggus.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "role")
public class Role {

    // == fields ==
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name="role_id")
    private int id;
    @Column(name="role")
    private String role;

    // == constructor ==
    public Role () {}

    public Role (int id, String role) {
        this.id = id;
        this.role = role;
    }
}
