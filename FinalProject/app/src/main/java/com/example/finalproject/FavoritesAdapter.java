package com.example.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * FavoritesAdapter is a custom ArrayAdapter used to display a list of NASAimage objects
 * within the ListView in FavoritesActivity. It loads images
 */
public class FavoritesAdapter extends ArrayAdapter<NASAimage> {

    private final Context context;
    private final List<NASAimage> favoriteImages;

    /**
     * Constructor for the adapter.
     *
     * @param context The calling context (FavoritesActivity).
     * @param images  The list of favorite NASA images to display.
     */
    public FavoritesAdapter(@NonNull Context context, @NonNull List<NASAimage> images) {
        super(context, 0, images);
        this.context = context;
        this.favoriteImages = images;
    }

    /**
     * Provides a custom view for each list item row.
     *
     * @param position    The position of the item in the list.
     * @param convertView The recycled view.
     * @param parent      The parent view group.
     * @return The view for the current item.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        // Inflate layout if it's not reused
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = convertView.findViewById(R.id.favoriteTitleTextView);
            holder.dateTextView = convertView.findViewById(R.id.favoriteDateTextView);
            holder.thumbnailImageView = convertView.findViewById(R.id.favoriteThumbnailImageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind data to the view
        NASAimage image = favoriteImages.get(position);
        holder.titleTextView.setText(image.getTitle());
        holder.dateTextView.setText(image.getDate());

        // Load thumbnail image asynchronously
        new ImageLoadTask(holder.thumbnailImageView).execute(image.getUrl());

        return convertView;
    }

    /**
     * ViewHolder pattern
     */
    private static class ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        ImageView thumbnailImageView;
    }

    /**
     * Loads an image from a URL  and sets it into an ImageView.
     *
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
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = imageViewRef.get();
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else if (imageView != null) {
                imageView.setImageResource(android.R.drawable.ic_dialog_alert); // fallback icon
            }
        }
    }
}
