package com.uade.api.models;

import com.uade.api.exceptions.InternalServerException;

import java.util.stream.Stream;

public enum AuctionStatus {
    OPEN("abierta"),
    CLOSED("cerrada");

    private String value;

    AuctionStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static AuctionStatus fromString(String status) {
        return Stream.of(AuctionStatus.values())
                .filter(userStatus -> userStatus.value().equals(status))
                .findFirst()
                .orElseThrow(InternalServerException::new);
    }
}
