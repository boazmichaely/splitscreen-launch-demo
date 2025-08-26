# Android Programmatic Split-Screen Demo

🚀 **Proof that Android apps CAN force themselves into split-screen mode programmatically**

## 🎯 The Discovery

This project demonstrates that Android applications can programmatically enter split-screen mode using `FLAG_ACTIVITY_LAUNCH_ADJACENT` - contradicting the common belief that this is impossible.

## ✨ Key Features

- **🔄 Programmatic Split-Screen Toggle**: Force your app into split-screen mode with one button tap
- **🎵 Adjacent Spotify Launch**: Launch Spotify in the adjacent window while staying visible
- **🎶 Adjacent YouTube Music Launch**: Launch YouTube Music in the adjacent window
- **📱 Multi-Window Detection**: Automatically detects and responds to split-screen state changes
- **🔧 Clean Implementation**: Simple, reliable code that works on Samsung devices

## 🛠️ Technical Implementation

### Core Mechanism
```java
// Force split-screen mode by launching dummy activity adjacent
Intent dummyIntent = new Intent(this, DummyActivity.class);
dummyIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(dummyIntent);
```

### App Launching in Split-Screen
```java
// Launch external apps in adjacent window when in multi-window mode
if (isInMultiWindowMode()) {
    intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);
}
startActivity(intent);
```

### Key Components
- **MainActivity**: Main interface with toggle and app launch buttons
- **DummyActivity**: Minimal activity that forces split-screen layout
- **Adjacent Launch Logic**: Detects multi-window state and launches apps accordingly

## 🧪 How to Test

1. **Install and run** the app on an Android device
2. **Tap "🔄 Enter Half Screen"** → Your app moves to top half, dummy activity appears in bottom half
3. **Tap "🎵 Launch Spotify"** → Spotify opens in bottom half (if installed)
4. **Tap gray area** → Return to full screen mode

## 📱 Tested On

- **Samsung Galaxy S23 Ultra** (SM-S936U)
- **Android API Level**: 26+ (minSdk 26, targetSdk 35)
- **Build Tools**: Android Gradle Plugin 8.9.0, Gradle 8.11.1

## 🎮 App Launch Strategies Tested

### Spotify
- **Method**: URI scheme (`spotify:`)
- **Behavior**: Works reliably with adjacent launch
- **Fallback**: Web player if app not installed

### YouTube Music  
- **Method**: Package manager (`getLaunchIntentForPackage()`)
- **Behavior**: Direct app launch with adjacent positioning
- **Fallback**: Web player if app not available

## 🔧 Requirements

- **Android Studio** 2024.1.1+
- **Minimum SDK**: 26 (Android 8.0)
- **Target SDK**: 35 (Android 15)
- **Device**: Physical device recommended (split-screen behavior varies on emulators)

## 🏗️ Project Structure

```
app/src/main/
├── java/com/test/splitscreen/
│   ├── MainActivity.java       # Main activity with controls
│   └── DummyActivity.java      # Minimal activity for forcing split-screen
├── res/
│   ├── layout/
│   │   └── activity_main.xml   # UI layout with toggle and launch buttons
│   ├── values/
│   │   └── strings.xml         # App strings
│   └── drawable/
│       └── ic_launcher.xml     # App icon
└── AndroidManifest.xml         # App configuration with resizable activities
```

## 🎯 Use Cases

- **Music Apps**: Launch music players while keeping your app visible
- **Multi-App Workflows**: Create seamless transitions between related apps
- **Productivity Tools**: Keep your app active while launching supporting tools
- **Media Control**: Control media apps while maintaining your interface

## 🤝 Contributing

This is a proof-of-concept demonstrating a little-known Android capability. Feel free to:

- Test on different devices and Android versions
- Expand with additional app launch methods
- Improve the exit mechanism
- Add support for different orientations

## ⚠️ Important Notes

- **Android Version**: Split-screen behavior varies across Android versions
- **Device Specific**: Some manufacturers may restrict multi-window capabilities
- **App Support**: Target apps must support multi-window mode
- **User Experience**: Consider UX implications of programmatic split-screen

## 📄 License

This project is provided as-is for educational and research purposes.

## 🙏 Acknowledgments

Discovered through experimentation with Android's multi-window APIs and the `FLAG_ACTIVITY_LAUNCH_ADJACENT` flag.

---

**Tags**: Android, Split-Screen, Multi-Window, FLAG_ACTIVITY_LAUNCH_ADJACENT, Programmatic UI, Mobile Development
