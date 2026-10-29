package com.example.smartpantrymanager.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.model.RecipeIngredient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PantryItem.class, Recipe.class, RecipeIngredient.class},
        version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static final ExecutorService IO = Executors.newSingleThreadExecutor();

    public abstract PantryDao pantryDao();

    public abstract RecipeDao recipeDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "pantry_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
