package com.example.smartpantrymanager.model;

import java.util.List;

public class RecipeWithIngredients {
    public final Recipe recipe;
    public final List<RecipeIngredient> ingredients;

    public RecipeWithIngredients(Recipe recipe, List<RecipeIngredient> ingredients) {
        this.recipe = recipe;
        this.ingredients = ingredients;
    }
}
