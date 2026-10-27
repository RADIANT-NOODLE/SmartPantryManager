package com.example.smartpantrymanager.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.smartpantrymanager.model.PantryItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PantryItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    /** Room refuses to run queries on the UI thread, so all DB work goes through this. */
    public static final ExecutorService IO = Executors.newSingleThreadExecutor();

    public abstract PantryDao pantryDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "pantry_db").build();
        }
        return instance;
    }
}
