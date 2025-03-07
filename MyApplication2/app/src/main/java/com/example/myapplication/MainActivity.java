package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText nameInput;
    private Switch secureSwitch;
    private Button sendButton;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Find views
            nameInput = findViewById(R.id.nameInput);
            secureSwitch = findViewById(R.id.secureSwitch);
            sendButton = findViewById(R.id.sendButton);

            // Set up button click listener
            sendButton.setOnClickListener(click -> {
                String name = nameInput.getText().toString();
                boolean isSecure = secureSwitch.isChecked();
            });
        }
    }