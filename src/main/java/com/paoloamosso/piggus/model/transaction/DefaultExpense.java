/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * Here the transaction domain
 */

package com.paoloamosso.piggus.model.transaction;

import java.util.HashMap;

// With Data with add getter and setter to each variable
public class DefaultExpense extends AbstractTransaction {

    public DefaultExpense() {
        super();
    }

    public DefaultExpense(HashMap<String, Object> objectHashMap) {
        super(objectHashMap);
    }
}
