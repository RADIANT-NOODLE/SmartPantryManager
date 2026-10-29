package com.example.smartpantrymanager.util;

import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.model.RecipeIngredient;
import com.example.smartpantrymanager.model.RecipeWithIngredients;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class RecipeMatcher {

    public static boolean ASSUME_ENOUGH_ON_UNIT_MISMATCH = false;

    private static final double EPS = 1e-9;

    private RecipeMatcher() { }

    public static List<RecipeWithIngredients> findStrictMatches(
            List<PantryItem> pantry, List<RecipeWithIngredients> recipes) {
        Map<String, Double> stock = buildStock(pantry);
        List<RecipeWithIngredients> result = new ArrayList<>();
        for (RecipeWithIngredients r : recipes) {
            if (canMake(stock, r)) result.add(r);
        }
        return result;
    }

    public static boolean canMake(List<PantryItem> pantry, RecipeWithIngredients recipe) {
        return canMake(buildStock(pantry), recipe);
    }

    private static boolean canMake(Map<String, Double> stock, RecipeWithIngredients recipe) {
        if (recipe.ingredients == null || recipe.ingredients.isEmpty()) return false;

        Map<String, Double> needed = new HashMap<>();
        for (RecipeIngredient ing : recipe.ingredients) {
            String key = key(ing.name, ing.unit);
            double amount = UnitConverter.toBase(ing.quantity, ing.unit);
            Double current = needed.get(key);
            needed.put(key, (current == null ? 0 : current) + amount);
        }

        for (Map.Entry<String, Double> e : needed.entrySet()) {
            Double have = stock.get(e.getKey());
            if (have != null && have + EPS >= e.getValue()) continue;   

            if (have == null && ASSUME_ENOUGH_ON_UNIT_MISMATCH
                    && hasSameNameInOtherUnit(stock, e.getKey())) {
                continue;                                                  
            }
            return false;                                                 
        }
        return true;
    }


    private static Map<String, Double> buildStock(List<PantryItem> pantry) {
        Map<String, Double> stock = new HashMap<>();
        if (pantry == null) return stock;
        for (PantryItem p : pantry) {
            if (p.quantity <= 0) continue;
            String key = key(p.name, p.unit);
            double amount = UnitConverter.toBase(p.quantity, p.unit);
            Double current = stock.get(key);
            stock.put(key, (current == null ? 0 : current) + amount);
        }
        return stock;
    }

    private static String key(String name, String unit) {
        return NameNormalizer.normalize(name) + "|" + UnitConverter.dimensionKey(unit);
    }

    private static boolean hasSameNameInOtherUnit(Map<String, Double> stock, String key) {
        String namePrefix = key.substring(0, key.indexOf('|') + 1);
        for (String k : stock.keySet()) {
            if (k.startsWith(namePrefix)) return true;
        }
        return false;
    }
}
