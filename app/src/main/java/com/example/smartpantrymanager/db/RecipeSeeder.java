package com.example.smartpantrymanager.db;

import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.model.RecipeIngredient;
import com.example.smartpantrymanager.util.NameNormalizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class RecipeSeeder {

    private RecipeSeeder() { }

    public static void seedIfEmpty(AppDatabase db) {
        if (db.recipeDao().countRecipes() > 0) return;

        db.runInTransaction(() -> {
            RecipeDao dao = db.recipeDao();
            for (Seed s : buildSeeds()) {
                long recipeId = dao.insertRecipe(new Recipe(s.name, s.steps));
                List<RecipeIngredient> rows = new ArrayList<>();
                for (RecipeIngredient ing : s.ingredients) {
                    ing.recipeId = (int) recipeId;
                    rows.add(ing);
                }
                dao.insertIngredients(rows);
            }
        });
    }


    private static class Seed {
        final String name, steps;
        final List<RecipeIngredient> ingredients;

        Seed(String name, String steps, RecipeIngredient... ings) {
            this.name = name;
            this.steps = steps;
            this.ingredients = Arrays.asList(ings);
        }
    }

    private static RecipeIngredient i(String name, double qty, String unit) {
        return new RecipeIngredient(0, NameNormalizer.normalize(name), qty, unit);
    }

    private static List<Seed> buildSeeds() {
        List<Seed> l = new ArrayList<>();

        l.add(new Seed("Scrambled Eggs",
                "1. Crack the eggs into a bowl and whisk.\n2. Melt the butter in a pan over low heat.\n3. Pour in the eggs and stir gently until just set.",
                i("egg", 3, "pcs"), i("butter", 10, "g")));

        l.add(new Seed("Cheese Omelette",
                "1. Whisk the eggs with the salt.\n2. Melt the butter in a pan and pour in the eggs.\n3. Add the grated cheese, fold the omelette and cook for 1 more minute.",
                i("egg", 3, "pcs"), i("cheese", 50, "g"), i("butter", 10, "g"), i("salt", 0.5, "tsp")));

        l.add(new Seed("Pancakes",
                "1. Whisk the flour, sugar, milk and egg into a smooth batter.\n2. Melt a little butter in a pan.\n3. Pour in small rounds of batter and cook 2 minutes per side.",
                i("flour", 150, "g"), i("milk", 250, "ml"), i("egg", 1, "pcs"),
                i("sugar", 1, "tbsp"), i("butter", 20, "g")));

        l.add(new Seed("Tomato Pasta",
                "1. Boil the pasta in salted water until tender.\n2. Fry the chopped onion and garlic in olive oil.\n3. Add the chopped tomatoes and simmer 10 minutes.\n4. Toss the drained pasta through the sauce.",
                i("pasta", 200, "g"), i("tomato", 4, "pcs"), i("onion", 1, "pcs"),
                i("garlic", 2, "pcs"), i("olive oil", 2, "tbsp"), i("salt", 1, "tsp")));

        l.add(new Seed("Garlic Butter Pasta",
                "1. Boil the pasta until tender and drain.\n2. Melt the butter and fry the crushed garlic for 1 minute.\n3. Toss the pasta in the garlic butter and season with salt.",
                i("pasta", 200, "g"), i("garlic", 3, "pcs"), i("butter", 30, "g"), i("salt", 0.5, "tsp")));

        l.add(new Seed("Egg Fried Rice",
                "1. Cook the rice and let it cool.\n2. Fry the chopped onion in oil.\n3. Push to the side, scramble the eggs in the pan.\n4. Add the rice and soy sauce and stir-fry for 4 minutes.",
                i("rice", 200, "g"), i("egg", 2, "pcs"), i("onion", 1, "pcs"),
                i("soy sauce", 2, "tbsp"), i("vegetable oil", 1, "tbsp")));

        l.add(new Seed("Chicken Stir-fry",
                "1. Slice the chicken and vegetables.\n2. Heat the oil in a wok and fry the chicken until golden.\n3. Add the onion, carrot and garlic and stir-fry 5 minutes.\n4. Add the soy sauce and cook 2 more minutes.",
                i("chicken", 300, "g"), i("onion", 1, "pcs"), i("carrot", 2, "pcs"),
                i("soy sauce", 2, "tbsp"), i("vegetable oil", 1, "tbsp"), i("garlic", 2, "pcs")));

        l.add(new Seed("Tomato Soup",
                "1. Fry the chopped onion and garlic in olive oil.\n2. Add the chopped tomatoes and the stock.\n3. Simmer 20 minutes, blend until smooth and season with salt.",
                i("tomato", 6, "pcs"), i("onion", 1, "pcs"), i("garlic", 2, "pcs"),
                i("vegetable stock", 500, "ml"), i("olive oil", 1, "tbsp"), i("salt", 1, "tsp")));

        l.add(new Seed("Cheese Toast",
                "1. Butter the bread.\n2. Top with the sliced or grated cheese.\n3. Grill or toast until the cheese is bubbling.",
                i("bread", 4, "pcs"), i("cheese", 80, "g"), i("butter", 10, "g")));

        l.add(new Seed("French Toast",
                "1. Whisk the eggs, milk and sugar together.\n2. Dip each slice of bread into the mixture.\n3. Fry in butter until golden on both sides.",
                i("bread", 4, "pcs"), i("egg", 2, "pcs"), i("milk", 100, "ml"),
                i("sugar", 1, "tbsp"), i("butter", 10, "g")));

        l.add(new Seed("Mashed Potatoes",
                "1. Peel, chop and boil the potatoes until soft.\n2. Drain and mash.\n3. Stir in the butter, warm milk and salt.",
                i("potato", 500, "g"), i("butter", 30, "g"), i("milk", 100, "ml"), i("salt", 1, "tsp")));

        l.add(new Seed("Roasted Potatoes",
                "1. Heat the oven to 200 C.\n2. Toss the chopped potatoes with the olive oil, crushed garlic and salt.\n3. Roast for 40 minutes, turning once.",
                i("potato", 600, "g"), i("olive oil", 2, "tbsp"), i("salt", 1, "tsp"), i("garlic", 2, "pcs")));

        l.add(new Seed("Banana Pancakes",
                "1. Mash the bananas in a bowl.\n2. Whisk in the eggs and flour.\n3. Fry small spoonfuls in a pan for 2 minutes per side.",
                i("banana", 2, "pcs"), i("egg", 2, "pcs"), i("flour", 50, "g")));

        l.add(new Seed("Creamy Porridge",
                "1. Put the oats, milk and sugar in a pot.\n2. Heat gently, stirring, for 5 minutes until thick.",
                i("oat", 80, "g"), i("milk", 300, "ml"), i("sugar", 1, "tsp")));

        l.add(new Seed("Guacamole",
                "1. Mash the avocados in a bowl.\n2. Stir in the finely chopped tomato and onion.\n3. Add lemon juice and salt to taste.",
                i("avocado", 2, "pcs"), i("tomato", 1, "pcs"), i("onion", 0.5, "pcs"),
                i("lemon", 1, "pcs"), i("salt", 0.5, "tsp")));

        l.add(new Seed("Vegetable Omelette",
                "1. Fry the chopped onion, tomato and bell pepper in oil for 3 minutes.\n2. Whisk the eggs with salt and pour over the vegetables.\n3. Cook until set and fold in half.",
                i("egg", 3, "pcs"), i("onion", 0.5, "pcs"), i("tomato", 1, "pcs"),
                i("bell pepper", 1, "pcs"), i("vegetable oil", 1, "tbsp"), i("salt", 0.5, "tsp")));

        l.add(new Seed("Tuna Pasta",
                "1. Boil the pasta until tender and drain.\n2. Warm the olive oil and fry the garlic for 1 minute.\n3. Add the drained tuna, then toss through the pasta and season.",
                i("pasta", 200, "g"), i("tuna", 1, "pcs"), i("olive oil", 1, "tbsp"),
                i("garlic", 1, "pcs"), i("salt", 0.5, "tsp")));

        l.add(new Seed("Lentil Soup",
                "1. Fry the chopped onion, carrot and garlic in a pot.\n2. Add the lentils, cumin and stock.\n3. Simmer 30 minutes until the lentils are soft.",
                i("lentil", 200, "g"), i("onion", 1, "pcs"), i("carrot", 2, "pcs"),
                i("garlic", 2, "pcs"), i("vegetable stock", 750, "ml"), i("cumin", 1, "tsp")));

        l.add(new Seed("Rice Pudding",
                "1. Put the rice, milk and sugar in a pot.\n2. Simmer on low heat for 30 minutes, stirring often, until creamy.",
                i("rice", 100, "g"), i("milk", 500, "ml"), i("sugar", 2, "tbsp")));

        l.add(new Seed("Peanut Butter Banana Toast",
                "1. Toast the bread.\n2. Spread with peanut butter.\n3. Top with sliced banana.",
                i("bread", 2, "pcs"), i("peanut butter", 2, "tbsp"), i("banana", 1, "pcs")));

        return l;
    }
}
