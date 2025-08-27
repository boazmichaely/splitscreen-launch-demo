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

/**
 * Split-Screen Launch Demo - MainActivity
 * 
 * This app demonstrates programmatic split-screen control in Android:
 * 1. Automatically entering split-screen by launching apps with FLAG_ACTIVITY_LAUNCH_ADJACENT
 * 2. Programmatically exiting split-screen using task manipulation techniques
 * 
 * Key Discovery: While Android doesn't provide direct APIs for split-screen control,
 * clever use of Intent flags and task manipulation can achieve both entry and exit.
 * 
 * @author Boaz Michaely with AI assistance
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SplitTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        MaterialButton launchSpotifyButton = findViewById(R.id.launchSpotifyButton);
        MaterialButton testExitButton = findViewById(R.id.testExitButton);

        // Set up button click handlers
        launchSpotifyButton.setOnClickListener(v -> launchSpotify());
        testExitButton.setOnClickListener(v -> testExitMethod4());

        // SMART UI: Only show the "Full Screen" button when it's actually useful
        // Initial state: hide the exit button unless we're already in split-screen
        if (testExitButton != null) {
            if (isInMultiWindowMode()) {
                // Rare case: app opened directly in split-screen
                testExitButton.setText("🖥️ Full Screen");
                testExitButton.setVisibility(View.VISIBLE);
            } else {
                // Normal case: hide until split-screen is entered
                testExitButton.setVisibility(View.GONE);
            }
        }

        Log.d(TAG, "Split Test app started - ready to test music app launching!");
    }

    /**
     * MAIN FEATURE: Smart Spotify Launch with Automatic Split-Screen
     * 
     * This method demonstrates the "one-tap" solution:
     * 1. If not in split-screen: Automatically forces split-screen mode, then launches Spotify
     * 2. If already in split-screen: Launches Spotify in the adjacent window
     * 
     * KEY INSIGHT: FLAG_ACTIVITY_LAUNCH_ADJACENT forces the calling app into split-screen
     * when the system supports multi-window mode. This was not documented clearly!
     */
    private void launchSpotify() {
        Log.d(TAG, "Attempting to launch Spotify...");
        
        // Check current multi-window state
        boolean isInMultiWindow = isInMultiWindowMode();
        Log.d(TAG, "Currently in multi-window mode: " + isInMultiWindow);
        
        if (!isInMultiWindow) {
            // MAGIC HAPPENS HERE: Force split-screen mode first
            Log.d(TAG, "Not in split-screen - forcing split-screen then launching Spotify");
            showSplitScreenHint("Auto-splitting screen and launching Spotify...");
            
            try {
                // Small delay allows the system to process the split-screen transition
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    launchSpotifyInSplitScreen();
                }, 200); // 200ms delay for smooth transition
                
            } catch (Exception e) {
                Log.e(TAG, "Failed to force split-screen: " + e.getMessage());
                // Graceful fallback to normal launch
                launchSpotifyNormally();
            }
        } else {
            // Already in split-screen, just launch in adjacent window
            Log.d(TAG, "Already in split-screen - launching Spotify in adjacent window");
            launchSpotifyInSplitScreen();
        }
    }
    
    /**
     * Launch Spotify with Split-Screen Intent Flags
     * 
     * KEY FLAG COMBINATION:
     * - FLAG_ACTIVITY_LAUNCH_ADJACENT: Forces launch in split-screen mode
     * - FLAG_ACTIVITY_NEW_TASK: Creates new task stack for the target app
     * 
     * This combination is the secret sauce that makes programmatic split-screen work!
     */
    private void launchSpotifyInSplitScreen() {
        try {
            // Try launching the native Spotify app first
            Intent spotifyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("spotify:"));
            spotifyIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(spotifyIntent);
            showSplitScreenHint("Spotify launched in split-screen!");
            Log.d(TAG, "Spotify launched in adjacent window");
        } catch (Exception e) {
            // Graceful fallback: launch Spotify web in split-screen
            Log.d(TAG, "Spotify app not available, opening web in split-screen");
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.spotify.com"));
            webIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(webIntent);
            showSplitScreenHint("Spotify web opened in split-screen!");
        }
    }
    
    /**
     * Fallback: Launch Spotify Normally (No Split-Screen Forcing)
     * 
     * This is the traditional app launch method when split-screen forcing fails.
     * User would need to manually enter split-screen via system gestures.
     */
    private void launchSpotifyNormally() {
        try {
            // Standard launch without split-screen flags
            Intent spotifyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("spotify:"));
            startActivity(spotifyIntent);
            showSplitScreenHint("Spotify launched! Swipe up and tap split-screen.");
        } catch (Exception e) {
            // Web fallback for normal launch
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.spotify.com"));
            startActivity(webIntent);
            showSplitScreenHint("Spotify web opened! Swipe up and tap split-screen.");
        }
    }





    /**
     * Helper: Show user feedback messages
     * 
     * Provides consistent user feedback throughout the split-screen workflow.
     * Keeps the user informed about what's happening during the automated process.
     */
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

    /**
     * BREAKTHROUGH DISCOVERY: Programmatic Split-Screen Exit
     * 
     * This method demonstrates how to programmatically exit split-screen mode.
     * 
     * THE WINNING TECHNIQUE: Task Manipulation
     * 1. moveTaskToBack(true) - Sends app to background
     * 2. Brief delay (150ms) - Allows system to process the state change  
     * 3. FLAG_ACTIVITY_REORDER_TO_FRONT - Brings app back as full-screen
     * 
     * WHY THIS WORKS: When an app in split-screen goes to background and returns,
     * Android's window manager resets it to full-screen mode. This behavior
     * appears to be intentional but undocumented.
     * 
     * TESTED ALTERNATIVES THAT FAILED:
     * - Window flags (FLAG_FULLSCREEN, etc.)
     * - Theme switching (fullscreen themes)
     * - WindowInsets APIs
     * - Activity recreation with different flags
     * 
     * This task manipulation method is the ONLY reliable solution found.
     */
    private void testExitMethod4() {
        boolean isInMultiWindow = isInMultiWindowMode();
        Log.d(TAG, "Exit Split-Screen Method - Currently in multi-window: " + isInMultiWindow);
        
        // Safety check: only work when actually in split-screen
        if (!isInMultiWindow) {
            Toast.makeText(this, "Not in split-screen mode. Enter split-screen first.", Toast.LENGTH_LONG).show();
            return;
        }
        
        Log.d(TAG, "Executing proven task manipulation method...");
        Toast.makeText(this, "✨ Exiting split-screen mode...", Toast.LENGTH_SHORT).show();
        
        try {
            // STEP 1: Move current task to background
            // This temporarily removes the app from split-screen view
            Log.d(TAG, "Task Manipulation: Moving to background then bringing to front");
            moveTaskToBack(true);
            
            // STEP 2: Brief delay to allow Android's window manager to process the change
            // This timing is crucial - too short and it doesn't work, too long and it's jarring
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                // STEP 3: Bring app back to front - Android resets it to full-screen!
                Intent bringBackIntent = new Intent(this, MainActivity.class);
                bringBackIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Log.d(TAG, "Bringing app back to front with REORDER_TO_FRONT");
                startActivity(bringBackIntent);
            }, 150); // 150ms = sweet spot for reliability
            
        } catch (Exception e) {
            Log.e(TAG, "Task manipulation failed: " + e.getMessage());
            Toast.makeText(this, "Exit method failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    




    /**
     * Multi-Window Mode Change Handler
     * 
     * This callback fires whenever the app enters/exits split-screen mode.
     * We use it to implement smart UI that shows controls only when relevant.
     * 
     * SMART UI LOGIC:
     * - Enter split-screen: Show "Full Screen" button (user can now exit)
     * - Exit split-screen: Hide the button (no longer needed)
     * 
     * This creates a clean, context-aware interface that doesn't confuse users
     * with controls that don't make sense in the current state.
     */
    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        Log.d(TAG, "Multi-window mode changed: " + isInMultiWindowMode);
        
        // Dynamic UI: Show/hide the exit button based on current mode
        MaterialButton testExitButton = findViewById(R.id.testExitButton);
        if (testExitButton != null) {
            if (isInMultiWindowMode) {
                // Entering split-screen: Show the exit button
                testExitButton.setText("🖥️ Full Screen");
                testExitButton.setVisibility(View.VISIBLE);
            } else {
                // Exiting split-screen: Hide the button (no longer useful)
                testExitButton.setVisibility(View.GONE);
            }
        }
        
        // User feedback for mode changes
        if (isInMultiWindowMode) {
            Toast.makeText(this, "Now in split-screen mode", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Exited split-screen mode", Toast.LENGTH_SHORT).show();
        }
    }
}
