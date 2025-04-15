package com.example.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Displays detailed information about a NASA image, including the title,
 * date, explanation, and the full image.
 */
public class ImageDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView explanationTextView;

    /**
     * Initializes the activity and sets up the UI elements.
     *
     * @param savedInstanceState the previously saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        // Setup toolbar with back navigation and title
        Toolbar toolbar = findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.image_details);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize UI views
        imageView = findViewById(R.id.detailImageView);
        titleTextView = findViewById(R.id.detailTitleTextView);
        dateTextView = findViewById(R.id.detailDateTextView);
        explanationTextView = findViewById(R.id.detailExplanationTextView);

        // Retrieve NASAimage object passed from the previous activity
        NASAimage image = (NASAimage) getIntent().getSerializableExtra("image");
        if (image != null) {
            titleTextView.setText(image.getTitle());
            dateTextView.setText(image.getDate());
            explanationTextView.setText(image.getExplanation());

            // Load image asynchronously to avoid blocking the UI
            new ImageLoadTask(imageView).execute(image.getUrl());
        }
    }

    /**
     * Handles toolbar back button functionality.
     *
     * @return true if handled
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * Inflate help menu.
     *
     * @param menu the menu to inflate
     * @return true if menu is displayed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Handle menu item selections (e.g., Help or Back).
     *
     * @param item selected menu item
     * @return true if handled
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_help) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.help))
                    .setMessage(getString(R.string.details_help))
                    .setPositiveButton(getString(R.string.yes), null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Loads an image from a URL  to display in an ImageView.
     */
    private static class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewRef;

        /**
         * Constructor that takes an ImageView to update once the image is loaded.
         *
         * @param imageView target ImageView
         */
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
            if (imageView != null) {
                if (result != null) {
                    imageView.setImageBitmap(result);
                } else {
                    // Display default error icon if loading fails
                    imageView.setImageResource(android.R.drawable.ic_dialog_alert);
                }
            }
        }
    }
}
