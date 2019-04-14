package com.paoloamosso.piggus.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnumUtils<T> {

    private Class<? extends Enum> enumClass;
    private Enum<?> enumObject;

    public EnumUtils (Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    public T stringToEnum(String input) {
        try {
            enumObject = Enum.valueOf(enumClass,input.toUpperCase());
        } catch (IllegalArgumentException iae) {
            log.error("The input in the enum conversion method was not valid. Input: {}, Enum: {} " +
                    "Error: {}",input,enumClass, iae.getMessage());
        }
        return (T) enumObject;
    }
}