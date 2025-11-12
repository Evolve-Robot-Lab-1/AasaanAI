# Durga AI Float Assistant

A production-ready Android Float Assistant that runs persistently, reads chat context, and generates persona-based AI replies.

## 📱 Overview

Durga AI Float Assistant is an intelligent Android overlay application that:
- Runs as a persistent floating bubble on your screen
- Extracts chat context from messaging apps using OCR + Accessibility
- Generates personalized, human-like replies based on your communication style
- Inserts replies seamlessly into chat applications
- Learns and adapts from your behavior over time

## ✅ Implementation Status

### Phase 0-1: Core Refactor ✓ COMPLETE
- ✅ Modular architecture with separated concerns
- ✅ `overlay/` - Floating bubble and service management
- ✅ `ai_engine/` - AI reply generation engine
- ✅ `persona/` - User personality and learning system
- ✅ `data_import/` - Persona import/export utilities
- ✅ `accessibility_bridge/` - Reply insertion via Accessibility API

### Phase 2-3: Persistent Overlay Service ✓ COMPLETE
- ✅ Foreground service (Android 12+ compliant)
- ✅ Auto-restart on boot via BroadcastReceiver
- ✅ Quick Settings Tile for easy toggle
- ✅ Persistent floating bubble with drag support
- ✅ Smooth Compose UI animations
- ✅ Battery-efficient implementation

### Upcoming Phases (4-8)
- 🔜 Phase 4-5: Screen reader + OCR fusion for chat extraction
- 🔜 Phase 6-7: AI engine with persona injection
- 🔜 Phase 8-9: Reply insertion with accessibility
- 🔜 Phase 10-15: UI enhancements, learning feedback, optimization

## 🏗️ Architecture

```
com.durgaai.assistant/
├── overlay/                    # Phase 2-3 ✓
│   ├── OverlayService.kt       # Foreground service
│   ├── FloatingBubble.kt       # Draggable bubble UI
│   ├── BootReceiver.kt         # Auto-restart on boot
│   └── OverlayTileService.kt   # Quick Settings tile
├── accessibility_bridge/       # Phase 4-5
│   ├── DurgaAccessibilityService.kt
│   └── ReplyInserter.kt
├── ai_engine/                  # Phase 6-7
│   └── AIEngine.kt
├── persona/                    # Phase 6-7
│   ├── PersonaEngine.kt
│   └── PersonaData.kt
└── data_import/                # Phase 6-7
    └── DataImporter.kt
```

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: 17 or higher
- **Android SDK**: API 26 (Android 8.0) or higher
- **Gradle**: 8.2+ (included via wrapper)

### Build Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/Evolve-Robot-Lab-1/AasaanAI.git
   cd AasaanAI
   ```

2. **Open in Android Studio**
   - File → Open → Select the AasaanAI directory
   - Wait for Gradle sync to complete

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Install on device/emulator**
   ```bash
   ./gradlew installDebug
   ```

   Or use Android Studio:
   - Click the "Run" button (▶️)
   - Select your target device

### Setup for Testing

#### 1. Grant Overlay Permission
- Open the Durga AI app
- Tap "1. Start Overlay Service"
- Grant "Display over other apps" permission
- The floating bubble will appear on your screen

#### 2. Enable Accessibility Service
- Tap "2. Enable Accessibility Service"
- Find "Durga AI" in the Accessibility settings
- Toggle it ON
- Grant permissions when prompted

#### 3. Quick Settings Tile (Optional)
- Swipe down notification shade twice
- Tap the edit button (pencil icon)
- Drag "AI Assistant" tile to your Quick Settings
- Use it to toggle the overlay on/off quickly

#### 4. Test Auto-Restart
- Restart your device
- The overlay will automatically start if it was previously enabled

## 🎯 Features (Phases 0-3)

### Persistent Overlay
- **Foreground Service**: Complies with Android 12+ requirements
- **Auto-Restart**: Survives device reboots
- **Low Priority**: Minimal battery impact
- **System Overlay**: Works across all apps

### Floating Bubble
- **Draggable**: Move anywhere on screen
- **Animated**: Pulsing effect for visual feedback
- **Compose UI**: Modern, smooth animations
- **Click Detection**: Distinguishes between drag and click

### Quick Access
- **Quick Settings Tile**: Toggle from notification shade
- **Boot Receiver**: Auto-start on device boot
- **Persistent State**: Remembers enabled/disabled state

### Modular Architecture
- **Separation of Concerns**: Each module handles specific functionality
- **Testability**: Independent module testing
- **Maintainability**: Easy to extend and modify
- **Scalability**: Ready for phases 4-8

## 🔧 Development

### Project Structure

```
AasaanAI/
├── app/
│   ├── src/main/
│   │   ├── java/com/durgaai/assistant/
│   │   │   ├── DurgaAIApplication.kt
│   │   │   ├── MainActivity.kt
│   │   │   ├── overlay/
│   │   │   ├── accessibility_bridge/
│   │   │   ├── ai_engine/
│   │   │   ├── persona/
│   │   │   └── data_import/
│   │   ├── res/
│   │   │   ├── values/
│   │   │   ├── xml/
│   │   │   └── drawable/
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

### Key Technologies

- **Kotlin**: 1.9.20
- **Jetpack Compose**: Latest BOM (2024.01.00)
- **Material 3**: Modern UI components
- **Coroutines**: Asynchronous operations
- **ML Kit**: OCR for text recognition (Phase 4-5)
- **OkHttp**: Network requests for AI API (Phase 6-7)
- **Gson**: JSON serialization for persona data

### Building Release APK

```bash
# Create signed release build
./gradlew assembleRelease

# Output location:
# app/build/outputs/apk/release/app-release.apk
```

**Note**: Configure signing in `app/build.gradle.kts` for production releases.

## 🧪 Testing

### Manual Testing Checklist

#### Overlay Service
- [ ] Overlay starts when "Start Overlay Service" is tapped
- [ ] Floating bubble appears on screen
- [ ] Bubble can be dragged around
- [ ] Bubble persists when switching apps
- [ ] Notification appears in notification shade

#### Auto-Restart
- [ ] Service restarts after device reboot
- [ ] Service only restarts if previously enabled
- [ ] Boot receiver handles both normal and quick boot

#### Quick Settings Tile
- [ ] Tile appears in Quick Settings editor
- [ ] Tile toggles overlay on/off
- [ ] Tile shows correct state (ON/OFF)
- [ ] Tile label updates correctly

#### Permission Handling
- [ ] Overlay permission request works
- [ ] App handles permission denial gracefully
- [ ] Accessibility settings open correctly

### Automated Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

## 📋 Requirements

### Minimum
- Android 8.0 (API 26)
- 2GB RAM
- 50MB storage

### Recommended
- Android 12+ (API 31+)
- 4GB RAM
- 100MB storage for future OCR models

## 🔐 Permissions

| Permission | Purpose | Phase |
|------------|---------|-------|
| `SYSTEM_ALERT_WINDOW` | Display floating bubble | 2-3 ✓ |
| `FOREGROUND_SERVICE` | Keep service running | 2-3 ✓ |
| `FOREGROUND_SERVICE_SPECIAL_USE` | Android 14+ compliance | 2-3 ✓ |
| `POST_NOTIFICATIONS` | Show service notification | 2-3 ✓ |
| `RECEIVE_BOOT_COMPLETED` | Auto-start on boot | 2-3 ✓ |
| `INTERNET` | AI API calls | 6-7 |
| `ACCESS_NETWORK_STATE` | Check connectivity | 6-7 |
| `VIBRATE` | Haptic feedback | 8-9 |

## 🗺️ Roadmap

### ✅ Completed
- **Week 1**: Persistent overlay + modular architecture (Phases 0-3)

### 🚧 In Progress
- **Week 2**: Screen reader + OCR + Persona-based AI (Phases 4-7)
- **Week 3**: Reply insertion + UI polish (Phases 8-10)
- **Week 4**: Adaptive learning + Beta release (Phases 11-15)

## 📝 License

Copyright © 2024 Durga AI. All rights reserved.

## 🤝 Contributing

This is a private development project. For issues or suggestions, contact the development team.

## 📞 Support

For technical support or questions:
- Open an issue in the repository
- Contact: [Your Contact Information]

## 🎉 Acknowledgments

Built with:
- Jetpack Compose for modern UI
- ML Kit for OCR capabilities
- Material Design 3 for consistent design language

---

**Status**: Phase 0-3 Complete ✅ | Ready for Testing
**Next**: Implement OCR + Accessibility for chat extraction (Phase 4-5)
