package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

/**
 * FavoritesActivity displays a list of previously saved NASA images.
 * Users can tap an image to view details, or long-press to delete it with confirmation.
 */
public class FavoritesActivity extends AppCompatActivity {

    private ListView favoritesListView;
    private TextView emptyView;
    private FavoritesManager favoritesManager;
    private FavoritesAdapter adapter;
    private List<NASAimage> favoriteImages;

    /**
     * Initializes the activity, loads saved images, and sets up the UI components.
     * @param savedInstanceState Saved instance state for configuration changes.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Set up the toolbar with title and back button
        Toolbar toolbar = findViewById(R.id.favoritesToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.favorites_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        favoritesListView = findViewById(R.id.favoritesListView);
        emptyView = findViewById(R.id.emptyView);

        // Load saved favorites from the manager
        favoritesManager = new FavoritesManager(this);
        favoriteImages = favoritesManager.getFavorites();

        // Set up the adapter to display saved images in the ListView
        adapter = new FavoritesAdapter(this, favoriteImages);
        favoritesListView.setAdapter(adapter);
        favoritesListView.setEmptyView(emptyView); // Show empty message if list is empty

        // Tap to open image details in new activity
        favoritesListView.setOnItemClickListener((parent, view, position, id) -> {
            NASAimage selected = favoriteImages.get(position);
            Intent intent = new Intent(FavoritesActivity.this, ImageDetailActivity.class);
            intent.putExtra("image", selected);
            startActivity(intent);
        });

        // Long press to confirm deletion of the favorite item
        favoritesListView.setOnItemLongClickListener((parent, view, position, id) -> {
            NASAimage selected = favoriteImages.get(position);

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete_confirm_title))
                    .setMessage(getString(R.string.delete_confirm_message))
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        favoritesManager.deleteFavorite(selected); // Remove from storage
                        favoriteImages.remove(position); // Remove from memory list
                        adapter.notifyDataSetChanged(); // Update the UI
                        Toast.makeText(this, getString(R.string.favorite_deleted), Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show();

            return true;
        });
    }

    /**
     * Inflate the options menu (Help and Favorites).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Handle toolbar menu selections (help and back button).
     * @param item The selected menu item.
     * @return true if handled, else call superclass.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Handle back button press
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        // Show help dialog
        else if (id == R.id.action_help) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.help))
                    .setMessage(getString(R.string.favorites_help))
                    .setPositiveButton(getString(R.string.yes), null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
