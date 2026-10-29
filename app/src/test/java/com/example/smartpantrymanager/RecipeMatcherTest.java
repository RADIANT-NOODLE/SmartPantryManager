package com.example.smartpantrymanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.model.RecipeIngredient;
import com.example.smartpantrymanager.model.RecipeWithIngredients;
import com.example.smartpantrymanager.util.RecipeMatcher;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeMatcherTest {

    private static PantryItem p(String name, double qty, String unit) {
        return new PantryItem(name, qty, unit, null);
    }

    private static RecipeIngredient ing(String name, double qty, String unit) {
        return new RecipeIngredient(0, name, qty, unit);
    }

    private static RecipeWithIngredients recipe(String name, RecipeIngredient... ings) {
        return new RecipeWithIngredients(new Recipe(name, "steps"), Arrays.asList(ings));
    }

    private static List<RecipeWithIngredients> match(List<PantryItem> pantry, RecipeWithIngredients... r) {
        return RecipeMatcher.findStrictMatches(pantry, Arrays.asList(r));
    }

    @Test public void fourOfFiveIngredients_isNotSuggested() {
        RecipeWithIngredients r = recipe("Five",
                ing("egg", 2, "pcs"), ing("flour", 100, "g"), ing("milk", 200, "ml"),
                ing("sugar", 1, "tbsp"), ing("butter", 20, "g"));
        List<PantryItem> pantry = Arrays.asList(
                p("egg", 2, "pcs"), p("flour", 100, "g"), p("milk", 200, "ml"),
                p("sugar", 1, "tbsp"));                     
        assertTrue(match(pantry, r).isEmpty());
    }

    @Test public void allFiveIngredients_isSuggested() {
        RecipeWithIngredients r = recipe("Five",
                ing("egg", 2, "pcs"), ing("flour", 100, "g"), ing("milk", 200, "ml"),
                ing("sugar", 1, "tbsp"), ing("butter", 20, "g"));
        List<PantryItem> pantry = Arrays.asList(
                p("egg", 2, "pcs"), p("flour", 100, "g"), p("milk", 200, "ml"),
                p("sugar", 1, "tbsp"), p("butter", 20, "g"));
        assertEquals(1, match(pantry, r).size());
    }

    @Test public void tooLittleQuantity_isNotSuggested() {
        RecipeWithIngredients r = recipe("Eggs", ing("egg", 3, "pcs"));
        assertTrue(match(Arrays.asList(p("egg", 2, "pcs")), r).isEmpty());
        assertEquals(1, match(Arrays.asList(p("egg", 3, "pcs")), r).size());
        assertEquals(1, match(Arrays.asList(p("egg", 12, "pcs")), r).size());
    }

    @Test public void singularAndPluralNamesMatch() {
        RecipeWithIngredients r = recipe("Tomato", ing("tomato", 2, "pcs"));
        assertEquals(1, match(Arrays.asList(p("Tomatoes", 2, "pcs")), r).size());
        assertEquals(1, match(Arrays.asList(p("  TOMATO ", 2, "pcs")), r).size());
    }

    @Test public void unitConversion_massAndVolume() {
        RecipeWithIngredients flour = recipe("Flour", ing("flour", 250, "g"));
        assertEquals(1, match(Arrays.asList(p("flour", 1, "kg")), flour).size());
        assertTrue(match(Arrays.asList(p("flour", 0.2, "kg")), flour).isEmpty());

        RecipeWithIngredients milk = recipe("Milk", ing("milk", 1, "cup"));
        assertEquals(1, match(Arrays.asList(p("milk", 500, "ml")), milk).size());
        assertTrue(match(Arrays.asList(p("milk", 200, "ml")), milk).isEmpty());

        RecipeWithIngredients oil = recipe("Oil", ing("oil", 3, "tsp"));
        assertEquals(1, match(Arrays.asList(p("oil", 1, "tbsp")), oil).size());
    }

    @Test public void severalPantryRowsAreAddedTogether() {
        RecipeWithIngredients r = recipe("Flour", ing("flour", 150, "g"));
        List<PantryItem> pantry = Arrays.asList(p("flour", 100, "g"), p("flour", 100, "g"));
        assertEquals(1, match(pantry, r).size());
    }

    @Test public void emptyPantry_matchesNothing() {
        RecipeWithIngredients r = recipe("Eggs", ing("egg", 1, "pcs"));
        assertTrue(match(new ArrayList<>(), r).isEmpty());
    }

    @Test public void recipeWithNoIngredients_isNeverSuggested() {
        RecipeWithIngredients r = recipe("Empty");
        assertTrue(match(Arrays.asList(p("egg", 1, "pcs")), r).isEmpty());
    }

    @Test public void onlyFullyCookableRecipesAreReturned() {
        RecipeWithIngredients ok = recipe("OK", ing("egg", 1, "pcs"));
        RecipeWithIngredients no = recipe("NO", ing("egg", 1, "pcs"), ing("cheese", 50, "g"));
        List<RecipeWithIngredients> res = match(Arrays.asList(p("egg", 6, "pcs")), ok, no);
        assertEquals(1, res.size());
        assertEquals("OK", res.get(0).recipe.name);
    }

    @Test public void incompatibleUnits_strictByDefault_lenientWhenSwitchedOn() {
        RecipeWithIngredients r = recipe("Salty", ing("salt", 1, "tsp"));
        List<PantryItem> pantry = Arrays.asList(p("salt", 100, "g"));  

        assertFalse(RecipeMatcher.ASSUME_ENOUGH_ON_UNIT_MISMATCH);
        assertTrue(match(pantry, r).isEmpty());

        RecipeMatcher.ASSUME_ENOUGH_ON_UNIT_MISMATCH = true;
        try {
            assertEquals(1, match(pantry, r).size());
            assertTrue(match(Arrays.asList(p("pepper", 100, "g")), r).isEmpty());
        } finally {
            RecipeMatcher.ASSUME_ENOUGH_ON_UNIT_MISMATCH = false;
        }
    }
}
