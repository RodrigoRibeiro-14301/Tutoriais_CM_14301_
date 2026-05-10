# PicsumGallery 🖼️

A clean Android image gallery app that fetches beautiful photos from the [Picsum Photos API](https://picsum.photos), displays them in a scrollable grid, and supports favorites, caching, and offline access.

## API Used

**Picsum Photos** — `https://picsum.photos/v2/list`

Free, no authentication required. Returns a list of images with metadata (id, author, width, height, download URL).

## Screenshots

> _(Add screenshots after running the app)_

| Main Screen | Image Details | Favorites |
|-------------|---------------|-----------|
| ![main]()   | ![details]()  | ![favs]() |

## How to Run

### Prerequisites
- Android Studio or AntiGravity IDE
- Android Emulator or physical device (API 26+)
- Internet connection (for first load)

### Steps

1. Clone or open this project in AntiGravity IDE
2. Let Gradle sync and resolve dependencies
3. Run the app on an emulator or device
4. The app will fetch images automatically on launch

### Build via AntiGravity

```
Build → Run Gradle Build → Deploy to Emulator/Device
```

## Tech Stack

- **Language:** Kotlin
- **UI:** XML Views (No Jetpack Compose)
- **Architecture:** MVVM
- **Networking:** Retrofit + OkHttp
- **Image Loading:** Glide
- **Async:** Coroutines + LiveData
- **Persistence:** Room Database (cache + favorites)
