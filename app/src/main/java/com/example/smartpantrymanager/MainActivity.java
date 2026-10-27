package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.smartpantrymanager.ui.PantryFragment;
import com.example.smartpantrymanager.ui.PlaceholderFragment;
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

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        BottomNavigationView nav = findViewById(R.id.bottomNav);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment f;
            if (id == R.id.nav_pantry) {
                f = new PantryFragment();
            } else if (id == R.id.nav_recipes) {
                f = PlaceholderFragment.newInstance(getString(R.string.coming_soon_recipes));
            } else {
                f = PlaceholderFragment.newInstance(getString(R.string.coming_soon_settings));
            }
            toolbar.setTitle(item.getTitle());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, f)
                    .commit();
            return true;
        });

        if (savedInstanceState == null) {
            nav.setSelectedItemId(R.id.nav_pantry);
        }
    }
}
