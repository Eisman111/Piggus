package com.paoloamosso.piggus.model.transaction;

public enum DefaultTransactionTypes implements TransactionTypes {

    // ==== enums ====
    DEFAULT_EXPENSE("DEFAULT_EXPENSE","DefaultExpense"),
    DEFAULT_INCOME("DEFAULT_INCOME","DefaultIncome");

    // ==== fields ====
    private String type;
    private String className;

    // ==== constructor ====
    DefaultTransactionTypes(String type, String className) {
        this.type = type;
        this.className = className;
    }

    // ==== methods ====
    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getClassName() {
        return this.className;
    }

    @Override
    public String toString() {
        return getType();
    }
}