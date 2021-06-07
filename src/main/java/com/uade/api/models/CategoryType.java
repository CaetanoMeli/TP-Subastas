package com.uade.api.models;

import com.uade.api.exceptions.InternalServerException;

import java.util.stream.Stream;

public enum CategoryType {
    COMMON("comun", 4),
    SPECIAL("especial", 3),
    SILVER("plata", 2),
    GOLD("oro", 1),
    PLATINUM("platino", 0);

    private String value;
    private int priority;

    CategoryType(String value, int priority) {
        this.value = value;
        this.priority = priority;
    }

    public String value() {
        return value;
    }

    public int priority() {
        return priority;
    }

    public static CategoryType fromString(String category) {
        return Stream.of(CategoryType.values())
                .filter(userStatus -> userStatus.value().equals(category))
                .findFirst()
                .orElseThrow(InternalServerException::new);
    }
}
