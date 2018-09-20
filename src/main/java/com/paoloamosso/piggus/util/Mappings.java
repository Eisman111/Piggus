/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * Here we should manage the name of the web request urls
 */

package com.paoloamosso.piggus.util;

// TODO improvement Priority 2: start using this
public class Mappings {

    // == constants ==
    // Here we define the paths to be shared internally
    public static final String HOME = "/";
    public static final String MONTHLY_EXPENSES = "monthly-expense";
    public static final String ADD_EXPENSE = "add-expense";
    public static final String LOGIN = "login";
    public static final String REGISTRATION = "register";

    // == constructor ==
    // We call the constructor private so that it can not be instantiated
    private Mappings () {}
}
