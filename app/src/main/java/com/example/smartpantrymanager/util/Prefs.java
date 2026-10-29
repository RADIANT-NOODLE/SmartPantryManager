package com.example.smartpantrymanager.util;

import android.content.Context;
import android.content.SharedPreferences;

public final class Prefs {

    private static final String FILE = "settings";
    private static final String KEY_EXPIRY_ALERTS = "expiry_alerts";
    private static final String KEY_EXPIRY_DAYS = "expiry_days";

    private Prefs() { }

    private static SharedPreferences sp(Context c) {
        return c.getApplicationContext().getSharedPreferences(FILE, Context.MODE_PRIVATE);
    }

    public static boolean expiryAlertsEnabled(Context c) {
        return sp(c).getBoolean(KEY_EXPIRY_ALERTS, true);
    }

    public static void setExpiryAlertsEnabled(Context c, boolean on) {
        sp(c).edit().putBoolean(KEY_EXPIRY_ALERTS, on).apply();
    }

    public static int expiryDays(Context c) {
        return sp(c).getInt(KEY_EXPIRY_DAYS, 3);
    }

    public static void setExpiryDays(Context c, int days) {
        sp(c).edit().putInt(KEY_EXPIRY_DAYS, days).apply();
    }
}
