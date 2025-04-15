package com.example.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

/**
 * Main activity of the NASA Explorer app. Allows users to select a date
 * and fetch NASA's Astronomy Picture of the Day. Users can also save
 * favorites and view HD images. Features a navigation drawer and toolbar.
 */
public class MainActivity extends AppCompatActivity {

    // UI Elements
    private DatePicker datePicker;
    private Button searchButton, hdImageButton, saveButton;
    private ImageView imageView;
    private TextView titleTextView, explanationTextView;
    private ProgressBar progressBar;

    // NASA Image Data
    private NASAimage currentImage;
    private NASAApiService apiService;
    private FavoritesManager favoritesManager;

    // Navigation Drawer
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerListView;

    /**
     * Initializes UI, listeners, drawer, and sets max date to today.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_title);
        }

        // Initialize UI elements
        datePicker = findViewById(R.id.datePicker);
        searchButton = findViewById(R.id.searchButton);
        hdImageButton = findViewById(R.id.hdImageButton);
        saveButton = findViewById(R.id.saveButton);
        imageView = findViewById(R.id.nasaImageView);
        titleTextView = findViewById(R.id.titleTextView);
        explanationTextView = findViewById(R.id.explanationTextView);
        progressBar = findViewById(R.id.progressBar);

        // Hide initially
        hdImageButton.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);

        // Managers
        apiService = new NASAApiService();
        favoritesManager = new FavoritesManager(this);

        // Setup date picker & event listeners
        setMaxDateToToday();
        setupListeners();

        // Setup Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        drawerListView = findViewById(R.id.navigation_view);
        String[] drawerItems = {getString(R.string.favorites_menu), getString(R.string.help)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawerItems);
        drawerListView.setAdapter(adapter);

        drawerListView.setOnItemClickListener((parent, view, position, id) -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            if (position == 0) {
                startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
            } else if (position == 1) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.help))
                        .setMessage(getString(R.string.main_help))
                        .setPositiveButton(getString(R.string.yes), null)
                        .show();
            }
        });
    }

    /**
     * Ensures users cannot select a date in the future.
     */
    private void setMaxDateToToday() {
        Calendar today = Calendar.getInstance();
        datePicker.setMaxDate(today.getTimeInMillis());
    }

    /**
     * Sets up listeners for search, HD image, and save buttons.
     */
    private void setupListeners() {
        searchButton.setOnClickListener(v -> fetchNASAImage(getSelectedDate()));

        hdImageButton.setOnClickListener(v -> {
            if (currentImage != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentImage.getHdurl()));
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(v -> {
            if (currentImage != null) {
                favoritesManager.saveFavorite(currentImage);
                Toast.makeText(this, R.string.image_saved, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Returns selected date as a formatted string (yyyy-MM-dd).
     */
    private String getSelectedDate() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    /**
     * Fetches NASA image using API and updates UI
     *
     * @param date the date string (yyyy-MM-dd)
     */
    private void fetchNASAImage(String date) {
        progressBar.setVisibility(View.VISIBLE);
        resetViews();

        apiService.fetchImageOfTheDay(date, new NASAApiService.Callback() {
            @Override
            public void onSuccess(NASAimage image) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    currentImage = image;
                    titleTextView.setText(image.getTitle());
                    explanationTextView.setText(image.getExplanation());
                    new ImageLoadTask(imageView).execute(image.getUrl());
                    hdImageButton.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.VISIBLE);
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, getString(R.string.error_message_prefix) + ": " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    /**
     * Clears content display when loading a new image.
     */
    private void resetViews() {
        imageView.setImageDrawable(null);
        titleTextView.setText("");
        explanationTextView.setText("");
        hdImageButton.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
    }

    /**
     * Loads image from URL into ImageView using AsyncTask.
     */
    private static class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewRef;

        public ImageLoadTask(ImageView imageView) {
            this.imageViewRef = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView imageView = imageViewRef.get();
            if (imageView != null && result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }

    /**
     * Inflate top-right toolbar menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Handles toolbar menu item clicks.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.help))
                    .setMessage(getString(R.string.main_help))
                    .setPositiveButton(getString(R.string.yes), null)
                    .show();
            return true;
        } else if (id == R.id.action_favorites) {
            startActivity(new Intent(this, FavoritesActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
