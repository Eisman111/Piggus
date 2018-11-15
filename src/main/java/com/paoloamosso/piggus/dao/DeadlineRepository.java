/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * Here we manage the connection between the service and the db
 */

package com.paoloamosso.piggus.dao;

import com.paoloamosso.piggus.model.Deadline;
import com.paoloamosso.piggus.model.User;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("deadlineRepository")
public interface DeadlineRepository extends JpaRepository<Deadline,Long> {

    List<Deadline> findDeadlinesByUser (User user);

    List<Deadline> findDeadlinesByIdParentDeadlineAndArchivedFalse (int idParent);

    Deadline findDeadlineById (int id);
}
