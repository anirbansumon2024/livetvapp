# Live TV Android App

একটি Android TV এবং Mobile দুই ডিভাইজ সাপোর্টকারী Live Streaming App।

## Features
- ✅ DASH (MPD) Stream Support
- ✅ HLS Stream Support  
- ✅ Mobile UI (Grid + Tab categories)
- ✅ Android TV UI (Sidebar + Large Grid)
- ✅ ExoPlayer with Media3
- ✅ GitHub Actions Auto Build

## Project Structure
```
app/src/main/
├── java/com/livetv/app/
│   ├── data/
│   │   ├── model/Channel.kt          # Data models
│   │   └── repository/ChannelRepository.kt
│   ├── player/
│   │   └── PlayerActivity.kt         # DASH/HLS Player
│   ├── ui/
│   │   ├── mobile/                   # Mobile UI
│   │   ├── tv/                       # TV UI
│   │   └── common/ChannelViewModel.kt
│   └── di/AppModule.kt
└── res/
    ├── layout/          # Mobile layouts
    └── layout-television/  # TV layouts (auto-selected on TV)
```

## Stream URL Configuration
`ChannelRepository.kt` ফাইলে আপনার stream URL গুলো যোগ করুন:
```kotlin
Channel("1", "My Channel", "logo_url",
    "https://your-server/stream/manifest.mpd", 
    StreamType.DASH, "News")
```

## Build করুন
```bash
./gradlew assembleDebug
```
APK পাবেন: `app/build/outputs/apk/debug/app-debug.apk`
