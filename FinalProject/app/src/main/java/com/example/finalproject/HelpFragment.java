package com.example.finalproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * HelpFragment is a reusable DialogFragment that displays a help message
 * to guide users on how to interact with the application's main features.
 * It retrieves local strings from the string resources.
 */
public class HelpFragment extends DialogFragment {

    /**
     * Called to create the dialog shown by the fragment.
     *
     * @param savedInstanceState Previous saved state, if available.
     * @return A new AlertDialog with title, message, and positive button.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setTitle(getString(R.string.help))  // Dialog title from string resources
                .setMessage(getString(R.string.main_help)) // Help message text
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> dismiss()); // OK button dismisses dialog

        return builder.create(); // Return constructed dialog
    }
}
