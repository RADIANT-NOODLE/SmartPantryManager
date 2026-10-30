package com.example.smartpantrymanager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smartpantrymanager.db.AppDatabase;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.model.RecipeIngredient;
import com.example.smartpantrymanager.util.Formatters;
import com.example.smartpantrymanager.util.NameNormalizer;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        if (recipeId == -1) { finish(); return; }

        TextView textIngredients = findViewById(R.id.textIngredients);
        TextView textSteps = findViewById(R.id.textSteps);

        AppDatabase db = AppDatabase.getInstance(this);
        AppDatabase.IO.execute(() -> {
            Recipe recipe = db.recipeDao().getRecipeById(recipeId);
            List<RecipeIngredient> ingredients = db.recipeDao().getIngredientsForRecipe(recipeId);

            mainHandler.post(() -> {
                if (recipe == null) { finish(); return; }
                if (getSupportActionBar() != null) getSupportActionBar().setTitle(recipe.name);

                StringBuilder sb = new StringBuilder();
                for (RecipeIngredient ing : ingredients) {
                    sb.append("\u2022 ")
                            .append(NameNormalizer.display(ing.name))
                            .append(" \u2013 ")
                            .append(Formatters.quantity(ing.quantity))
                            .append(' ')
                            .append(ing.unit)
                            .append('\n');
                }
                textIngredients.setText(sb.toString().trim());
                textSteps.setText(recipe.steps);
            });
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
