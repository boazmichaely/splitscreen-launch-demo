package com.test.splitscreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SplitTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton launchSpotifyButton = findViewById(R.id.launchSpotifyButton);
        MaterialButton testExitButton = findViewById(R.id.testExitButton);

        launchSpotifyButton.setOnClickListener(v -> launchSpotify());
        testExitButton.setOnClickListener(v -> testExitMethod4());

        // Set initial test button state
        if (testExitButton != null) {
            if (isInMultiWindowMode()) {
                testExitButton.setText("🖥️ Full Screen");
            } else {
                testExitButton.setText("🧪 Enter Split-Screen First");
            }
        }

        Log.d(TAG, "Split Test app started - ready to test split-screen control!");
    }

    private void launchSpotify() {
        Log.d(TAG, "Launching Spotify...");
        
        try {
            Intent spotifyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("spotify:"));
            
            // If in split-screen, launch in adjacent window
            if (isInMultiWindowMode()) {
                spotifyIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d(TAG, "Launching Spotify in adjacent window (split-screen)");
            } else {
                Log.d(TAG, "Launching Spotify normally");
            }
            
            startActivity(spotifyIntent);
            Toast.makeText(this, "Spotify launched", Toast.LENGTH_SHORT).show();
            
        } catch (Exception e) {
            // Fallback to web
            Log.d(TAG, "Spotify app not available, opening web version");
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.spotify.com"));
            if (isInMultiWindowMode()) {
                webIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            startActivity(webIntent);
            Toast.makeText(this, "Opened Spotify web", Toast.LENGTH_SHORT).show();
        }
    }





    @Override
    protected void onResume() {
        super.onResume();
        boolean isInMultiWindow = isInMultiWindowMode();
        Log.d(TAG, "Split Test app resumed (Multi-window: " + isInMultiWindow + ")");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Split Test app paused");
    }

    private void testExitMethod4() {
        boolean isInMultiWindow = isInMultiWindowMode();
        Log.d(TAG, "Exit Split-Screen Method - Currently in multi-window: " + isInMultiWindow);
        
        if (!isInMultiWindow) {
            Toast.makeText(this, "Not in split-screen mode. Enter split-screen first.", Toast.LENGTH_LONG).show();
            return;
        }
        
        Log.d(TAG, "Executing task manipulation method...");
        Toast.makeText(this, "Exiting split-screen mode...", Toast.LENGTH_SHORT).show();
        
        try {
            // THE PROVEN METHOD: Task manipulation (moveTaskToBack + reorder to front)
            Log.d(TAG, "Task Manipulation: Moving to background then bringing to front");
            moveTaskToBack(true);
            
            // Brief delay then bring back to front - this forces Android to exit split-screen!
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                Intent bringBackIntent = new Intent(this, MainActivity.class);
                bringBackIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Log.d(TAG, "Bringing app back to front with REORDER_TO_FRONT");
                startActivity(bringBackIntent);
            }, 150); // Slightly longer delay for reliability
            
        } catch (Exception e) {
            Log.e(TAG, "Task manipulation failed: " + e.getMessage());
            Toast.makeText(this, "Exit method failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    




    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        Log.d(TAG, "Multi-window mode changed: " + isInMultiWindowMode);
        
        // Update test button visibility and text
        MaterialButton testExitButton = findViewById(R.id.testExitButton);
        if (testExitButton != null) {
            if (isInMultiWindowMode) {
                testExitButton.setText("🖥️ Full Screen");
                testExitButton.setVisibility(View.VISIBLE);
            } else {
                testExitButton.setText("🧪 Enter Split-Screen First");
                testExitButton.setVisibility(View.VISIBLE);
            }
        }
        
        if (isInMultiWindowMode) {
            Toast.makeText(this, "Now in split-screen mode", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Exited split-screen mode", Toast.LENGTH_SHORT).show();
        }
    }
}
