package com.example.smartpantrymanager.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String name;
    public String steps;    

    public Recipe(@NonNull String name, String steps) {
        this.name = name;
        this.steps = steps;
    }
}
