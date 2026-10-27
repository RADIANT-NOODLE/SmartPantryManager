package com.example.smartpantrymanager.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Turns ingredient names into one canonical form so that "Tomatoes", " tomato "
 * and "TOMATO" all become "tomato". The recipe matcher (Increment 3) reuses this.
 */
public final class NameNormalizer {

    // Words that end in "s" but are NOT plurals.
    private static final Set<String> KEEP_AS_IS = new HashSet<>(Arrays.asList(
            "hummus", "couscous", "asparagus", "molasses", "swiss", "citrus",
            "lemongrass", "watercress", "cress", "bass", "grass"));

    // Words ending in "oes" whose singular is just "-o" (tomatoes -> tomato).
    private static final Set<String> OES_WORDS = new HashSet<>(Arrays.asList(
            "tomatoes", "potatoes", "mangoes", "avocadoes", "jalapenoes"));

    private NameNormalizer() { }

    public static String normalize(String raw) {
        if (raw == null) return "";
        String s = raw.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
        if (s.isEmpty()) return s;

        // Singularise only the last word: "red onions" -> "red onion".
        int space = s.lastIndexOf(' ');
        String head = space >= 0 ? s.substring(0, space + 1) : "";
        String last = space >= 0 ? s.substring(space + 1) : s;
        return head + singular(last);
    }

    private static String singular(String w) {
        if (w.length() <= 3 || KEEP_AS_IS.contains(w)) return w;
        if (OES_WORDS.contains(w)) return w.substring(0, w.length() - 2);
        if (w.endsWith("ies")) return w.substring(0, w.length() - 3) + "y";     // berries -> berry
        if (w.endsWith("ches") || w.endsWith("shes") || w.endsWith("sses")
                || w.endsWith("xes")) {
            return w.substring(0, w.length() - 2);                              // peaches -> peach
        }
        if (w.endsWith("ss") || w.endsWith("us")) return w;                     // watercress, hummus
        if (w.endsWith("s")) return w.substring(0, w.length() - 1);             // onions -> onion
        return w;
    }

    /** "red onion" -> "Red onion" for display. */
    public static String display(String normalized) {
        if (normalized == null || normalized.isEmpty()) return "";
        return normalized.substring(0, 1).toUpperCase(Locale.ROOT) + normalized.substring(1);
    }
}
