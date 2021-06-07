package com.uade.api.models;

import com.uade.api.exceptions.InternalServerException;

import java.util.stream.Stream;

public enum DepositStatus {
    YES("si"),
    NO("no");

    private String value;

    DepositStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static DepositStatus fromString(String status) {
        return Stream.of(DepositStatus.values())
                .filter(userStatus -> userStatus.value().equals(status))
                .findFirst()
                .orElseThrow(InternalServerException::new);
    }
}
