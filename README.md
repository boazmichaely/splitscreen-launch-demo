# Split Screen Launch Demo

## Android Split-Screen Programmatic Control

This project demonstrates programmatic control of Android's split-screen mode, addressing gaps in common knowledge. While the Android docs suggest limited programmatic control over multi-window mode, community knowledge and experimentation reveal workable approaches.

Most of the ideas here came from [this Stack Overflow thread](https://stackoverflow.com/questions/26543268/android-making-a-fullscreen-application).

This demo provides working code for both entering and exiting split-screen mode programmatically.

## What This App Does

### Launch Spotify Button
- One-tap workflow: Automatically launches Spotify 
- Works whether you're in split-screen or fullscreen
- Uses URI scheme (`spotify:`) with web fallback

### Full Screen Button
- Reliable method to programmatically exit split-screen
- Uses task manipulation: `moveTaskToBack()` + `FLAG_ACTIVITY_REORDER_TO_FRONT`
- Forces Android to exit split-screen mode

## Technical Implementation

### Programmatic Split-Screen Entry
Uses `FLAG_ACTIVITY_LAUNCH_ADJACENT` to force split-screen mode by launching Spotify directly:
```java
// Launch Spotify in adjacent window to trigger split-screen
Intent spotifyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("spotify:"));
spotifyIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(spotifyIntent);
```

### Programmatic Split-Screen Exit
Uses task manipulation approach from Stack Overflow community:
```java
// Task manipulation method
moveTaskToBack(true);

Handler handler = new Handler(Looper.getMainLooper());
handler.postDelayed(() -> {
    Intent bringBackIntent = new Intent(this, MainActivity.class);
    bringBackIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    startActivity(bringBackIntent);
}, 150);
```


## User Interface

Simple 2-button interface:
- Launch Spotify - One tap to launch Spotify (auto split-screen if not already in it)
- Full Screen - One tap to exit split-screen

The buttons adapt based on current mode with visual feedback via Toast messages.

## Core Technologies

- Material Design 3 for modern Android UI
- Intent System for deep linking and activity management  
- Multi-Window APIs: `isInMultiWindowMode()`, `onMultiWindowModeChanged()`
- Handler/Looper for precise timing control
- Package Manager for app detection and launching

## Key Components

- `MainActivity.java` - Core logic and UI
- `activity_main.xml` - 2-button layout
- `AndroidManifest.xml` - Multi-window permissions

## Installation & Usage

1. Clone the repository
2. Open in Android Studio  
3. Run on device (split-screen requires physical device)
4. Test the implementation:
   - Tap "Launch Spotify" to launch Spotify (auto split-screen)
   - Tap "Full Screen" to exit split-screen mode

## What This Demo Provides

Working implementations for:
- Programmatic split-screen entry using `FLAG_ACTIVITY_LAUNCH_ADJACENT`
- Programmatic split-screen exit using task manipulation
- Simple one-tap workflows for common use cases
- Clean, documented code ready for integration

This demo bridges the gap between unclear official documentation and practical implementation needs.

## Development Notes

Developed with AI assistance - This project was created through collaboration between human direction and AI implementation, combining community knowledge with systematic testing approaches.

## License

Open source - feel free to study, modify, and build upon this implementation.