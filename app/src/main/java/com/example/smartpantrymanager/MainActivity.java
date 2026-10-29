package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.smartpantrymanager.db.AppDatabase;
import com.example.smartpantrymanager.db.RecipeSeeder;
import com.example.smartpantrymanager.ui.PantryFragment;
import com.example.smartpantrymanager.ui.RecipesFragment;
import com.example.smartpantrymanager.ui.SettingsFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AppDatabase db = AppDatabase.getInstance(this);
        AppDatabase.IO.execute(() -> RecipeSeeder.seedIfEmpty(db));

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        BottomNavigationView nav = findViewById(R.id.bottomNav);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment f;
            int title;
            if (id == R.id.nav_pantry) {
                f = new PantryFragment();
                title = R.string.title_pantry;
            } else if (id == R.id.nav_recipes) {
                f = new RecipesFragment();
                title = R.string.title_suggested;
            } else {
                f = new SettingsFragment();
                title = R.string.title_settings;
            }
            toolbar.setTitle(title);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, f)
                    .commit();
            return true;
        });

        if (savedInstanceState == null) {
            nav.setSelectedItemId(R.id.nav_pantry);
        } else {
            toolbar.setTitle(titleFor(nav.getSelectedItemId()));
        }
    }

    private static int titleFor(int navId) {
        if (navId == R.id.nav_recipes) return R.string.title_suggested;
        if (navId == R.id.nav_settings) return R.string.title_settings;
        return R.string.title_pantry;
    }
}
