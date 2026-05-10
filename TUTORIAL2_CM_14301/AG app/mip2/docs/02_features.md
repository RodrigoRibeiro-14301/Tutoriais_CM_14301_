# 02 — Features

## Core Features

1. **Fetch Images from API**
   Retrieve a paginated list of photos from `https://picsum.photos/v2/list` using Retrofit.

2. **Display Images in a Grid**
   Show images in a 2-column RecyclerView grid using `GridLayoutManager`.

3. **Swipe to Refresh**
   Pull down on the main screen to reload images from the API.

4. **Loading Indicator**
   Show a `ProgressBar` while images or data are being fetched. The indicator is relative to what is being loaded (initial load vs. pagination vs. refresh).

5. **Image Details Screen**
   Tap any image to open a details screen showing:
   - Full-size image
   - Author name
   - Image dimensions
   - Download URL

6. **Favorite Items (FIFO Queue, max 5)**
   - Users can mark any image as a favorite.
   - Maximum of 5 favorites stored at a time.
   - When a 6th item is added, the oldest favorite is automatically removed (FIFO).
   - A persistent favorites strip is visible at the top of every screen, showing up to 5 thumbnail images for direct access.

7. **Image Cache (max 50 non-favorite items)**
   - Loaded images are cached in a local Room database.
   - Cache holds a maximum of 50 items (not counting favorites).
   - During scrolling, at least 10 items ahead and 10 behind the current position are kept loaded.
   - Old cache entries beyond the 50-item limit are evicted automatically.

8. **Offline Access**
   - When no internet connection is available, the app displays cached images.
   - Favorites are always available offline.
   - An offline banner is displayed to inform the user.

9. **Error Handling**
   - API errors (timeouts, 4xx, 5xx) are caught and displayed with a user-friendly message.
   - A **Retry** button is shown when an error occurs.
