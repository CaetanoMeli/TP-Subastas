package com.uade.api.models;

import com.uade.api.exceptions.InternalServerException;

import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

public enum TimeUnit {
    SECONDS("seconds", ChronoUnit.SECONDS),
    MINUTES("minutes", ChronoUnit.MINUTES),
    HOURS("hours", ChronoUnit.HOURS);

    private String value;
    private ChronoUnit chronoUnit;

    TimeUnit(String value, ChronoUnit chronoUnit) {
        this.value = value;
        this.chronoUnit = chronoUnit;
    }

    public String value() {
        return value;
    }

    public ChronoUnit chronoUnit() {
        return chronoUnit;
    }

    public static TimeUnit fromString(String unit) {
        return Stream.of(TimeUnit.values())
                .filter(userStatus -> userStatus.value().equals(unit))
                .findFirst()
                .orElseThrow(InternalServerException::new);
    }
}
