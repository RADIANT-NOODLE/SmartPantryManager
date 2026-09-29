package com.example.smartpantrymanager.model;   // keep whatever yours says

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pantry_items")
public class PantryItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String name;        // stored normalised, e.g. "tomato"
    public double quantity;
    public String unit;
    public String expiryDate;  // optional, "yyyy-MM-dd" or null

    public PantryItem(@NonNull String name, double quantity, String unit, String expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }
}