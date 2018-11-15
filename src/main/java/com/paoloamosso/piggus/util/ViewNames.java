/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * Here we should manage the name of the viewnames
 */

package com.paoloamosso.piggus.util;

// TODO improvement Priority 2: start using this
public final class ViewNames {

    // == constants ==
    // Here we define the view names (html) path to be shared internally
    public static final String HOME = "index";
    public static final String MONTHLY_EXPENSES = "monthly-transaction";
    public static final String ADD_EXPENSE = "add-transaction";
    public static final String LOGIN = "login";
    public static final String REGISTRATION = "register";

    // == constructor ==
    // We call the constructor private so that it can not be instantiated
    private ViewNames () {}
}
