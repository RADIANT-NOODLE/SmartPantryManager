package com.example.smartpantrymanager;

import static org.junit.Assert.assertEquals;

import com.example.smartpantrymanager.util.NameNormalizer;

import org.junit.Test;

public class NameNormalizerTest {

    @Test public void trimsAndLowercases() {
        assertEquals("tomato", NameNormalizer.normalize("  TOMATO "));
    }

    @Test public void pluralsMatchSingulars() {
        assertEquals("tomato", NameNormalizer.normalize("Tomatoes"));
        assertEquals("potato", NameNormalizer.normalize("potatoes"));
        assertEquals("onion", NameNormalizer.normalize("onions"));
        assertEquals("berry", NameNormalizer.normalize("berries"));
        assertEquals("peach", NameNormalizer.normalize("peaches"));
        assertEquals("egg", NameNormalizer.normalize("eggs"));
    }

    @Test public void onlyLastWordIsSingularised() {
        assertEquals("red onion", NameNormalizer.normalize("Red  Onions"));
    }

    @Test public void nonPluralsAreKept() {
        assertEquals("hummus", NameNormalizer.normalize("hummus"));
        assertEquals("couscous", NameNormalizer.normalize("couscous"));
        assertEquals("asparagus", NameNormalizer.normalize("asparagus"));
    }

    @Test public void displayCapitalises() {
        assertEquals("Red onion", NameNormalizer.display("red onion"));
    }
}
