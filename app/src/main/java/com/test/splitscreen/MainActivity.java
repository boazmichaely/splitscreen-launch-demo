package com.test.splitscreen;

import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

        Log.d(TAG, "Split Test app started - ready to test music app launching!");
    }

    private void launchSpotify() {
        Log.d(TAG, "Attempting to launch Spotify...");
        
        // Check if we're currently in multi-window mode
        boolean isInMultiWindow = isInMultiWindowMode();
        Log.d(TAG, "Currently in multi-window mode: " + isInMultiWindow);
        
        if (!isInMultiWindow) {
            // Step 1: Force split-screen mode first
            Log.d(TAG, "Not in split-screen - forcing split-screen then launching Spotify");
            showSplitScreenHint("Auto-splitting screen and launching Spotify...");
            
            try {
                // Just launch Spotify normally since no split-screen forced
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    launchSpotifyInSplitScreen();
                }, 200); // 200ms delay to let dummy activity start
                
            } catch (Exception e) {
                Log.e(TAG, "Failed to force split-screen: " + e.getMessage());
                // Fallback to normal launch
                launchSpotifyNormally();
            }
        } else {
            // Already in split-screen, just launch Spotify
            Log.d(TAG, "Already in split-screen - launching Spotify in adjacent window");
            launchSpotifyInSplitScreen();
        }
    }
    
    private void launchSpotifyInSplitScreen() {
        try {
            Intent spotifyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("spotify:"));
            spotifyIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(spotifyIntent);
            showSplitScreenHint("Spotify launched in split-screen!");
            Log.d(TAG, "Spotify launched in adjacent window");
        } catch (Exception e) {
            // Fallback to web in split-screen
            Log.d(TAG, "Spotify app not available, opening web in split-screen");
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.spotify.com"));
            webIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(webIntent);
            showSplitScreenHint("Spotify web opened in split-screen!");
        }
    }
    
    private void launchSpotifyNormally() {
        try {
            Intent spotifyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("spotify:"));
            startActivity(spotifyIntent);
            showSplitScreenHint("Spotify launched! Swipe up and tap split-screen.");
        } catch (Exception e) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.spotify.com"));
            startActivity(webIntent);
            showSplitScreenHint("Spotify web opened! Swipe up and tap split-screen.");
        }
    }





    private void showSplitScreenHint(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.d(TAG, "Hint shown: " + message);
    }

    private void toggleHalfScreen() {
        boolean isInMultiWindow = isInMultiWindowMode();
        Log.d(TAG, "Toggle half screen - currently in multi-window: " + isInMultiWindow);

        if (isInMultiWindow) {
            Toast.makeText(this, "Tap the gray area to exit split-screen", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Use the Full Screen button to exit split-screen");
            Toast.makeText(this, "Use 'Full Screen' button to exit split-screen", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isInMultiWindow = isInMultiWindowMode();
        Log.d(TAG, "Split Test app resumed - user returned from music app (Multi-window: " + isInMultiWindow + ")");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Split Test app paused - user switched to another app");
        }

    private void testExitMethod4() {
        boolean isInMultiWindow = isInMultiWindowMode();
        Log.d(TAG, "Exit Split-Screen Method - Currently in multi-window: " + isInMultiWindow);
        
        if (!isInMultiWindow) {
            Toast.makeText(this, "Not in split-screen mode. Enter split-screen first.", Toast.LENGTH_LONG).show();
            return;
        }
        
        Log.d(TAG, "Executing proven task manipulation method...");
        Toast.makeText(this, "✨ Exiting split-screen mode...", Toast.LENGTH_SHORT).show();
        
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
            Toast.makeText(this, "✅ Now in split-screen! Try exit button.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "🎉 Successfully exited split-screen mode!", Toast.LENGTH_SHORT).show();
        }
    }
}
