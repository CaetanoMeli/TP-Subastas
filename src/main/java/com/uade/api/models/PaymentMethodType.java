package com.uade.api.models;

import com.uade.api.exceptions.InternalServerException;

import java.util.stream.Stream;

public enum PaymentMethodType {
    CREDIT_CARD("credit_card"),
    BANK_ACCOUNT("bank_account");

    private String value;

    PaymentMethodType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static PaymentMethodType fromString(String type) {
        return Stream.of(PaymentMethodType.values())
                .filter(userStatus -> userStatus.value().equals(type))
                .findFirst()
                .orElseThrow(InternalServerException::new);
    }
}
