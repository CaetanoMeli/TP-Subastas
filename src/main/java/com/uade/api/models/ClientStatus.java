package com.uade.api.models;


import com.uade.api.exceptions.InternalServerException;

import java.util.stream.Stream;

public enum ClientStatus {
    ADMITTED("si"),
    NOT_ADMITTED("no");

    private String value;

    ClientStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static ClientStatus fromString(String status) {
        return Stream.of(ClientStatus.values())
                .filter(userStatus -> userStatus.value().equals(status))
                .findFirst()
                .orElseThrow(InternalServerException::new);
    }
}
