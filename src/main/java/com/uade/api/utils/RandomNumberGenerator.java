package com.uade.api.utils;

public class RandomNumberGenerator {

    public static String generateRandomNumber() {
        return String.format("%06d", Math.round(Math.random() * 999999));
    }
}
