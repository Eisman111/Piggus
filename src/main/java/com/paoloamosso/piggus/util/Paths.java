package com.paoloamosso.piggus.util;

import lombok.Getter;
import lombok.Setter;

public enum Paths {
    // ==== enums ====
    TRANSACTION_DEFAULT_PATH ("com.paoloamosso.piggus.model.transaction.");

    // ==== fields ====
    private String path;

    // ==== constructor ====
    Paths(String path) {
        this.path = path;
    }

    // ==== methods ====
    public String getPath(){
        return this.path;
    }
}
