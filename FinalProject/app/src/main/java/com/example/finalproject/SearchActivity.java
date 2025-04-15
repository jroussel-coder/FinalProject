package com.example.finalproject;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

/**
 * SearchActivity handles the action for a simple search button
 * that shows a Snackbar when clicked.
 *
 * This activity satisfies the project requirement to include
 * a button and a Snackbar.
 */
public class SearchActivity extends AppCompatActivity {

    // UI button for triggering the search action
    private Button searchButton;

    /**
     * Called when the activity is first created.
     * Sets the layout and initializes the search button.
     *
     * @param savedInstanceState Saved state if activity is reloaded
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Link button to layout element
        searchButton = findViewById(R.id.searchButton);

        // Set button click listener to perform search
        searchButton.setOnClickListener(v -> performSearch());
    }

    /**
     * Displays a Snackbar indicating that a search is being performed.
     * Placeholder for future search logic.
     */
    private void performSearch() {
        Snackbar.make(findViewById(R.id.searchLayout),
                "Searching...",
                Snackbar.LENGTH_SHORT).show();
    }
}
