package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages the saving, loading, and deletion of favorite NASA images
 * using SharedPreferences for persistent local storage.
 */
public class FavoritesManager {

    private static final String PREFS_NAME = "nasa_favorites_prefs"; // Preference file name
    private static final String KEY_IMAGES = "favorite_images";       // Key to store image data

    private final SharedPreferences prefs;

    /**
     * Constructor initializes SharedPreferences.
     *
     * @param context Application context for accessing preferences.
     */
    public FavoritesManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Saves a NASAimage to favorites if it's not already saved.
     *
     * @param image NASAimage to be saved.
     */
    public void saveFavorite(NASAimage image) {
        List<NASAimage> images = getFavorites();
        for (NASAimage img : images) {
            if (img.getDate().equals(image.getDate())) return; // Avoid duplicates by date
        }
        images.add(image);
        saveList(images);
    }

    /**
     * Deletes a favorite NASAimage based on its date.
     *
     * @param image NASAimage to be removed.
     */
    public void deleteFavorite(NASAimage image) {
        List<NASAimage> images = getFavorites();
        images.removeIf(i -> i.getDate().equals(image.getDate()));
        saveList(images);
    }

    /**
     * Loads the list of favorite NASAimage objects from preferences.
     *
     * @return List of saved NASAimage objects.
     */
    public List<NASAimage> getFavorites() {
        Set<String> storedSet = prefs.getStringSet(KEY_IMAGES, new HashSet<>());
        List<NASAimage> list = new ArrayList<>();
        for (String item : storedSet) {
            NASAimage img = deserialize(item);
            if (img != null) list.add(img);
        }
        return list;
    }

    /**
     * Serializes and stores the list of NASAimage objects in preferences.
     *
     * @param images List of NASAimage objects to store.
     */
    private void saveList(List<NASAimage> images) {
        Set<String> storeSet = new HashSet<>();
        for (NASAimage image : images) {
            storeSet.add(serialize(image));
        }
        prefs.edit().putStringSet(KEY_IMAGES, storeSet).apply();
    }

    /**
     * Converts a NASAimage object into a string format for storage.
     * Replaces any "|" characters to prevent parsing issues.
     *
     * @param image NASAimage to serialize.
     * @return  string representation of the image.
     */
    private String serialize(NASAimage image) {
        return image.getDate() + "|" +
                image.getTitle().replace("|", " ") + "|" +
                image.getUrl() + "|" +
                image.getHdurl() + "|" +
                image.getExplanation().replace("|", " ");
    }

    /**
     * Converts a serialized string back into a NASAimage object.
     *
     * @param string  string format of a NASAimage.
     * @return NASAimage object or null if invalid.
     */
    private NASAimage deserialize(String string) {
        String[] parts = string.split("\\|", 5);
        if (parts.length < 5) return null;
        return new NASAimage(parts[0], parts[1], parts[4], parts[2], parts[3]);
    }
}
