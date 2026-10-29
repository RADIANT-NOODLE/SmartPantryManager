package com.example.smartpantrymanager.util;

public final class Formatters {

    private Formatters() { }

    public static String quantity(double q) {
        return q == Math.floor(q) ? String.valueOf((long) q) : String.valueOf(q);
    }
}
