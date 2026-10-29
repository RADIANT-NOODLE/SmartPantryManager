package com.example.smartpantrymanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.RecipeDetailActivity;
import com.example.smartpantrymanager.db.AppDatabase;
import com.example.smartpantrymanager.db.RecipeRepository;
import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.model.RecipeWithIngredients;
import com.example.smartpantrymanager.util.RecipeMatcher;

import java.util.List;

public class RecipesFragment extends Fragment implements RecipeAdapter.Listener {

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private RecipeAdapter adapter;
    private TextView emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipes, container, false);
        emptyView = root.findViewById(R.id.textEmptyRecipes);
        RecyclerView recycler = root.findViewById(R.id.recyclerRecipes);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RecipeAdapter(this);
        recycler.setAdapter(adapter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSuggestions();  
    }

    private void loadSuggestions() {
        AppDatabase db = AppDatabase.getInstance(requireContext());
        AppDatabase.IO.execute(() -> {
            List<PantryItem> pantry = db.pantryDao().getAll();
            List<RecipeWithIngredients> all = RecipeRepository.loadAll(db);
            List<RecipeWithIngredients> matches = RecipeMatcher.findStrictMatches(pantry, all);

            mainHandler.post(() -> {
                if (!isAdded()) return;
                adapter.setItems(matches);
                emptyView.setVisibility(matches.isEmpty() ? View.VISIBLE : View.GONE);
            });
        });
    }

    @Override
    public void onRecipeClick(RecipeWithIngredients r) {
        Intent intent = new Intent(requireContext(), RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, r.recipe.id);
        startActivity(intent);
    }
}
