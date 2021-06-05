package com.uade.api.models;


import com.uade.api.exceptions.InternalServerException;

import java.util.stream.Stream;

public enum UserStatus {
    ACTIVE("activo"),
    INACTIVE("inactivo");

    private String value;

    UserStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static UserStatus fromString(String status) {
        return Stream.of(UserStatus.values())
                .filter(userStatus -> userStatus.value().equals(status))
                .findFirst()
                .orElseThrow(InternalServerException::new);
    }
}
