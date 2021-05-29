package com.uade.api.models;

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
}
