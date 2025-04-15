package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manages application preferences using Android's SharedPreferences.
 * Stores user settings such as whether this is the first launch and dark mode preference.
 */
public class PreferenceManager {

    // Preference file name
    private static final String PREF_NAME = "NASAImagePrefs";

    // Preference keys
    private static final String KEY_FIRST_LAUNCH = "first_launch";
    private static final String KEY_DARK_MODE = "dark_mode";

    private final SharedPreferences sharedPreferences;

    /**
     * Constructs a PreferenceManager for handling user settings.
     *
     * @param context Application context for accessing SharedPreferences.
     */
    public PreferenceManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Sets whether the app is being launched for the first time.
     *
     * @param isFirstLaunch true if first launch, false otherwise.
     */
    public void setFirstLaunch(boolean isFirstLaunch) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_FIRST_LAUNCH, isFirstLaunch);
        editor.apply(); // Save changes asynchronously
    }

    /**
     * Returns whether this is the first time the app is launched.
     *
     * @return true if it's the first launch; false otherwise.
     */
    public boolean isFirstLaunch() {
        return sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    /**
     * Enables or disables dark mode preference.
     *
     * @param isDarkMode true to enable dark mode; false to disable.
     */
    public void setDarkMode(boolean isDarkMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_DARK_MODE, isDarkMode);
        editor.apply(); // Save changes asynchronously
    }

    /**
     * Checks whether dark mode is enabled.
     *
     * @return true if dark mode is enabled; false otherwise.
     */
    public boolean isDarkModeEnabled() {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false);
    }
}
