/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * This is the bridge between the repository and the controller
 */

package com.paoloamosso.piggus.service;

import com.paoloamosso.piggus.dao.RoleRepository;
import com.paoloamosso.piggus.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("roleService")
public class RoleService {

    // == fields ==
    @Autowired
    @Qualifier("roleRepository")
    private RoleRepository roleRepository;

    // == constructor ==
    // This is necessary to create the default role and avoid errors on empty db
    public void createDefaultRole() {
        if (roleRepository.findByRole("USER") == null) {
            roleRepository.save(new Role(1,"USER"));
        }
    }
}
