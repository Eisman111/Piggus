/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * Here the transaction domain
 */

package com.paoloamosso.piggus.model.transaction;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.HashMap;

@Entity
@DiscriminatorValue("DEFAULT_EXPENSE")
public class DefaultExpense extends Transaction {

    public DefaultExpense() {
        super();
    }

    public DefaultExpense(HashMap<String, Object> objectHashMap) {
        super(objectHashMap);
    }
}
