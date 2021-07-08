package com.uade.api.models;

import com.uade.api.exceptions.InternalServerException;

import java.util.stream.Stream;

public enum CatalogStatus {
    AUCTIONED("Subastado"),
    TO_AUCTION("A Subastarse"),
    AUCTIONING("Subastandose");

    private String value;

    CatalogStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static CatalogStatus fromString(String category) {
        return Stream.of(CatalogStatus.values())
                .filter(userStatus -> userStatus.value().equals(category))
                .findFirst()
                .orElseThrow(() -> new InternalServerException("invalid_category"));
    }
}
