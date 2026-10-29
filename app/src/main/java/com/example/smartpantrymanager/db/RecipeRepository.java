package com.example.smartpantrymanager.db;

import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.model.RecipeIngredient;
import com.example.smartpantrymanager.model.RecipeWithIngredients;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RecipeRepository {

    private RecipeRepository() { }

    public static List<RecipeWithIngredients> loadAll(AppDatabase db) {
        List<Recipe> recipes = db.recipeDao().getAllRecipes();
        List<RecipeIngredient> all = db.recipeDao().getAllIngredients();

        Map<Integer, List<RecipeIngredient>> byRecipe = new HashMap<>();
        for (RecipeIngredient ing : all) {
            List<RecipeIngredient> list = byRecipe.get(ing.recipeId);
            if (list == null) {
                list = new ArrayList<>();
                byRecipe.put(ing.recipeId, list);
            }
            list.add(ing);
        }

        List<RecipeWithIngredients> result = new ArrayList<>();
        for (Recipe r : recipes) {
            List<RecipeIngredient> ings = byRecipe.get(r.id);
            result.add(new RecipeWithIngredients(r, ings == null ? new ArrayList<>() : ings));
        }
        return result;
    }
}
