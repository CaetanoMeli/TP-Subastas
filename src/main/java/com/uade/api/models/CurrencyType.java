package com.uade.api.models;


import com.uade.api.exceptions.InternalServerException;

import java.util.stream.Stream;

public enum CurrencyType {
    USD("usd", "U$D"),
    ARS("ars", "$");

    private String type;
    private String currencyId;

    CurrencyType(String type, String currencyId) {
        this.type = type;
        this.currencyId = currencyId;
    }

    public String value() {
        return type;
    }

    public String currencyId() {
        return currencyId;
    }

    public static CurrencyType fromString(String type) {
        return Stream.of(CurrencyType.values())
                .filter(userStatus -> userStatus.value().equals(type))
                .findFirst()
                .orElseThrow(InternalServerException::new);
    }
}
