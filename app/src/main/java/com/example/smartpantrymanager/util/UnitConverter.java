package com.example.smartpantrymanager.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class UnitConverter {

    private static final Map<String, String> ALIASES = new HashMap<>();
    private static final Map<String, Double> FACTOR = new HashMap<>();
    private static final Map<String, String> DIMENSION = new HashMap<>();

    private static void unit(String canonical, String dimension, double factor, String... aliases) {
        FACTOR.put(canonical, factor);
        DIMENSION.put(canonical, dimension);
        ALIASES.put(canonical, canonical);
        for (String a : aliases) ALIASES.put(a, canonical);
    }

    static {
        unit("g", "MASS", 1, "gram", "grams", "gr");
        unit("kg", "MASS", 1000, "kilogram", "kilograms", "kgs");
        unit("ml", "VOLUME", 1, "milliliter", "milliliters", "millilitre", "millilitres");
        unit("l", "VOLUME", 1000, "liter", "liters", "litre", "litres", "lt");
        unit("tsp", "VOLUME", 5, "teaspoon", "teaspoons");
        unit("tbsp", "VOLUME", 15, "tablespoon", "tablespoons", "tbs");
        unit("cup", "VOLUME", 240, "cups");
        unit("pcs", "COUNT", 1, "pc", "piece", "pieces", "unit", "units", "whole");
    }

    private UnitConverter() { }

    public static String canonicalUnit(String unit) {
        if (unit == null) return "";
        String u = unit.trim().toLowerCase(Locale.ROOT);
        String c = ALIASES.get(u);
        return c != null ? c : u;
    }

    public static String dimensionKey(String unit) {
        String c = canonicalUnit(unit);
        String d = DIMENSION.get(c);
        return d != null ? d : "UNIT:" + c;
    }

    public static double toBase(double quantity, String unit) {
        Double f = FACTOR.get(canonicalUnit(unit));
        return quantity * (f == null ? 1.0 : f);
    }
}
