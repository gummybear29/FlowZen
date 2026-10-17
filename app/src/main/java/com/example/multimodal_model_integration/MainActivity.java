package com.example.multimodal_model_integration;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private static final int REQUEST_CODE_GALLERY = 2;

    private EditText editText;
    private ImageButton sendButton, micButton, galleryButton;
    private ProgressBar progressBar;
    private TextView predictionResult;

    private Uri selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        editText = findViewById(R.id.textual_search_bar_ID);
        sendButton = findViewById(R.id.send_button);
        micButton = findViewById(R.id.mic_button);
        galleryButton = findViewById(R.id.gallery_button);
        progressBar = findViewById(R.id.progress_bar);
        predictionResult = findViewById(R.id.prediction_result);

        // Handle send button click (text or image prediction)
        sendButton.setOnClickListener(v -> handleSend());

        // Start voice recognition
        micButton.setOnClickListener(v -> startVoiceRecognition());

        // Open gallery to pick image
        galleryButton.setOnClickListener(v -> openGallery());
    }

    // Handle send button logic
    private void handleSend() {
        String textInput = editText.getText().toString().trim();

        if (selectedImageUri != null) {
            predictionResult.setText("Sending image for prediction...");
            progressBar.setVisibility(View.VISIBLE);

            // TODO: Replace with image-based prediction
            editText.postDelayed(() -> {
                progressBar.setVisibility(View.GONE);
                predictionResult.setText("Predicted result for selected image.");
                selectedImageUri = null;  // Clear image URI after processing
            }, 2000);

        } else if (!textInput.isEmpty()) {
            predictionResult.setText("Sending text for prediction...");
            progressBar.setVisibility(View.VISIBLE);

            // TODO: Replace with text-based prediction
            editText.postDelayed(() -> {
                progressBar.setVisibility(View.GONE);
                predictionResult.setText("Predicted result for: " + textInput);
            }, 2000);
        } else {
            Toast.makeText(this, "Please enter a query or select an image", Toast.LENGTH_SHORT).show();
        }
    }

    // Launch voice input
    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Voice input not supported", Toast.LENGTH_SHORT).show();
        }
    }

    // Open gallery to choose image
    private void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 101);
                return;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                return;
            }
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    // Handle results from voice or gallery input
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result != null && !result.isEmpty()) {
                    editText.setText(result.get(0));
                }

            } else if (requestCode == REQUEST_CODE_GALLERY) {
                selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    predictionResult.setText("Image selected. Press send to predict.");
                }
            }
        }
    }
}
