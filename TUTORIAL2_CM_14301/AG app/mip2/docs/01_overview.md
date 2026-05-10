# 01 — Project Overview

## Application Name

**PicsumGallery**

## Purpose

PicsumGallery is an Android application that allows users to browse a curated gallery of high-quality photographs fetched from the Picsum Photos public API. Users can scroll through images, view detailed information about each photo and its author, mark images as favorites, and access previously loaded content even without an internet connection.

## Target Users

- Students learning Android development who want a real-world reference app.
- Casual users who enjoy browsing photography.
- Developers exploring MVVM architecture, caching strategies, and offline-first design.

## How the System Works

1. On launch, the app requests a paginated list of images from `https://picsum.photos/v2/list`.
2. The images are displayed in a scrollable grid on the **Main Screen**.
3. As the user scrolls, new images are loaded automatically (pagination), maintaining a cache window of 10 items ahead and 10 behind the current position.
4. Tapping an image opens the **Image Details Screen**, showing the full photo and metadata.
5. Users can mark images as **favorites** (max 5, FIFO queue). Favorites are always accessible via a persistent strip at the top of any screen.
6. All loaded images are cached locally using Room (max 50 non-favorite items).
7. If the device is offline, the app serves content from the local cache gracefully.
8. API errors are caught and shown to the user with a retry option.
