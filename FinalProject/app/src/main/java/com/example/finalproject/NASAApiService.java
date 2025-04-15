package com.example.finalproject;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * NASAApiService is responsible for retrieving NASA's Astronomy Picture of the Day
 * by sending HTTP requests to the NASA APOD API.
 */
public class NASAApiService {

    private static final String BASE_URL = "https://api.nasa.gov/planetary/apod";
    private static final String API_KEY = "DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d"; // Public NASA key (can be replaced)

    /**
     * Callback interface used to communicate results of async API request.
     */
    public interface Callback {
        /**
         * Called when image data is successfully fetched and parsed.
         * @param image Parsed NASAimage object
         */
        void onSuccess(NASAimage image);

        /**
         * Called when an error occurs during the fetch operation.
         * @param errorMessage Description of the error
         */
        void onError(String errorMessage);
    }

    /**
     * Public method to initiate fetching an image for the specified date.
     * @param date Date string in format yyyy-MM-dd
     * @param callback Callback for success or error
     */
    public void fetchImageOfTheDay(String date, Callback callback) {
        new FetchImageTask(callback).execute(date);
    }

    /**
     * AsyncTask to handle the background HTTP request and JSON parsing.
     */
    private static class FetchImageTask extends AsyncTask<String, Void, NASAimage> {

        private final Callback callback;
        private String errorMessage;

        public FetchImageTask(Callback callback) {
            this.callback = callback;
        }

        /**
         * Performs the HTTP GET request to NASA's  API in the background.
         * @param params The date to request
         * @return A NASAimage object if successful, null otherwise
         */
        @Override
        protected NASAimage doInBackground(String... params) {
            String date = params[0];
            try {
                URL url = new URL(BASE_URL + "?api_key=" + API_KEY + "&date=" + date);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                // Parse JSON response into NASAimage object
                JSONObject jsonResponse = new JSONObject(response.toString());
                NASAimage image = new NASAimage();
                image.setDate(jsonResponse.getString("date"));
                image.setTitle(jsonResponse.getString("title"));
                image.setExplanation(jsonResponse.getString("explanation"));
                image.setUrl(jsonResponse.getString("url"));
                image.setHdurl(jsonResponse.optString("hdurl", jsonResponse.getString("url"))); // fallback if hdurl missing

                return image;
            } catch (Exception e) {
                Log.e("NASAApiService", "Error fetching NASA image: ", e);
                errorMessage = e.getMessage();
                return null;
            }
        }

        /**
         * Handles the result of the background operation and invokes the appropriate callback.
         * @param image The NASAimage result or null if there was an error
         */
        @Override
        protected void onPostExecute(NASAimage image) {
            if (image != null) {
                callback.onSuccess(image);
            } else {
                callback.onError(errorMessage != null ? errorMessage : "Unknown error");
            }
        }
    }
}
