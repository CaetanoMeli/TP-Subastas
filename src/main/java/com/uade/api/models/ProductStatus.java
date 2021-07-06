package com.uade.api.models;

import com.uade.api.exceptions.InternalServerException;

import java.util.stream.Stream;

public enum ProductStatus {
    PENDING_APPROVAL("pending_approval"),
    PENDING_CONFIRMATION("pending_confirmation"),
    ASSIGNED_AUCTION("assigned_auction"),
    SOLD("sold");

    private String value;

    ProductStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static ProductStatus fromString(String status) {
        return Stream.of(ProductStatus.values())
                .filter(userStatus -> userStatus.value().equals(status))
                .findFirst()
                .orElseThrow(() -> new InternalServerException("invalid_status"));
    }
}
