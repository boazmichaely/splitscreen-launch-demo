package com.test.splitscreen;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SplitTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if we're restarting to exit multi-window mode
        boolean exitingMultiWindow = getIntent().getBooleanExtra("exit_multiwindow", false);
        if (exitingMultiWindow) {
            Log.d(TAG, "Restarted MainActivity to exit multi-window mode");
            Toast.makeText(this, "✅ Returned to full screen!", Toast.LENGTH_SHORT).show();
        }

        MaterialButton launchSpotifyButton = findViewById(R.id.launchSpotifyButton);
        MaterialButton launchYouTubeMusicButton = findViewById(R.id.launchYouTubeMusicButton);
        MaterialButton showInstructionsButton = findViewById(R.id.showInstructionsButton);
        MaterialButton toggleHalfScreenButton = findViewById(R.id.toggleHalfScreenButton);

        launchSpotifyButton.setOnClickListener(v -> launchSpotify());
        launchYouTubeMusicButton.setOnClickListener(v -> launchYouTubeMusic());
        showInstructionsButton.setOnClickListener(v -> showSplitScreenInstructions());
        toggleHalfScreenButton.setOnClickListener(v -> toggleHalfScreen());

        // Set initial button text based on current mode
        updateToggleButtonText();

        Log.d(TAG, "Split Test app started - ready to test music app launching!");
    }

    private void launchSpotify() {
        Log.d(TAG, "Attempting to launch Spotify...");
        
        // Check if we're currently in multi-window mode
        boolean isInMultiWindow = isInMultiWindowMode();
        Log.d(TAG, "Currently in multi-window mode: " + isInMultiWindow);
        
        try {
            // Launch Spotify using URI scheme (works reliably)
            Intent spotifyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("spotify:"));
            
            if (isInMultiWindow) {
                // Try to launch in adjacent window (split-screen)
                spotifyIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d(TAG, "Launching Spotify in adjacent window (split-screen mode)");
                showSplitScreenHint("Attempting to launch Spotify in adjacent window...");
            } else {
                // Normal launch for non-split-screen mode
                Log.d(TAG, "Launching Spotify normally (not in split-screen)");
                showSplitScreenHint("Spotify launched! Now swipe up and tap split-screen.");
            }
            
            startActivity(spotifyIntent);
            Log.d(TAG, "Spotify app launched successfully");
            
        } catch (Exception e) {
            // Fallback to web if URI scheme fails
            Log.d(TAG, "Spotify app not available, opening web version");
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.spotify.com"));
            
            if (isInMultiWindow) {
                webIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
                showSplitScreenHint("Opening Spotify web in adjacent window...");
            } else {
                showSplitScreenHint("Spotify web opened! Now swipe up and tap split-screen.");
            }
            
            startActivity(webIntent);
        }
    }

    private void launchYouTubeMusic() {
        Log.d(TAG, "Attempting to launch YouTube Music...");
        
        // Check if we're currently in multi-window mode
        boolean isInMultiWindow = isInMultiWindowMode();
        Log.d(TAG, "Currently in multi-window mode: " + isInMultiWindow);
        
        try {
            // Try to launch YouTube Music app using package manager
            Intent youtubeMusicIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.youtube.music");
            if (youtubeMusicIntent != null) {
                if (isInMultiWindow) {
                    // Try to launch in adjacent window (split-screen)
                    youtubeMusicIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Log.d(TAG, "Launching YouTube Music in adjacent window (split-screen mode)");
                    showSplitScreenHint("Attempting to launch YouTube Music in adjacent window...");
                } else {
                    Log.d(TAG, "Launching YouTube Music normally (not in split-screen)");
                    showSplitScreenHint("YouTube Music launched! Now swipe up and tap split-screen.");
                }
                startActivity(youtubeMusicIntent);
                Log.d(TAG, "YouTube Music app launched successfully");
            } else {
                // Fallback to web if app not available
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://music.youtube.com"));
                if (isInMultiWindow) {
                    webIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    showSplitScreenHint("Opening YouTube Music web in adjacent window...");
                } else {
                    showSplitScreenHint("YouTube Music web opened! Now swipe up and tap split-screen.");
                }
                startActivity(webIntent);
                Log.d(TAG, "YouTube Music web player launched");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error launching YouTube Music", e);
            Toast.makeText(this, "Could not launch YouTube Music", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSplitScreenInstructions() {
        new AlertDialog.Builder(this)
            .setTitle("Split Screen Guide")
            .setMessage("To use split-screen with music apps:\n\n" +
                       "Method 1 - Quick Launch:\n" +
                       "1. Tap Launch Spotify or Launch YouTube Music\n" +
                       "2. Swipe up to see recent apps\n" +
                       "3. Tap the Split Test icon at top\n" +
                       "4. Select Split screen\n" +
                       "5. Choose your music app\n\n" +
                       "Goal: Test which method feels better!")
            .setPositiveButton("Got it!", null)
            .show();
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
            Log.d(TAG, "Entering half-screen mode...");
            Intent dummyIntent = new Intent(this, DummyActivity.class);
            dummyIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dummyIntent);
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
    
    private void updateToggleButtonText() {
        MaterialButton toggleButton = findViewById(R.id.toggleHalfScreenButton);
        if (toggleButton != null) {
            boolean isInMultiWindow = isInMultiWindowMode();
            if (isInMultiWindow) {
                toggleButton.setText("🔄 Exit Half Screen");
            } else {
                toggleButton.setText("🔄 Enter Half Screen");
            }
        }
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        Log.d(TAG, "Multi-window mode changed: " + isInMultiWindowMode);
        
        // Update toggle button text based on mode
        updateToggleButtonText();
        
        if (isInMultiWindowMode) {
            Toast.makeText(this, "✅ Now in split-screen! Try launching music apps.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "❌ Exited split-screen mode.", Toast.LENGTH_SHORT).show();
        }
    }
}
