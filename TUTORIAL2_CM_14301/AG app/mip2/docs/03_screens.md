# 03 — Screens

## Screen 1: Main Screen (`MainActivity`)

**File:** `MainActivity.kt` + `activity_main.xml`

### Components

- **Toolbar** — App title "PicsumGallery" + Favorites icon button (top-right)
- **Favorites Strip** — Horizontal `RecyclerView` at the top showing up to 5 favorite thumbnails. Tapping a thumbnail opens the Image Details Screen for that item. Hidden when no favorites exist.
- **SwipeRefreshLayout** — Wraps the main grid to allow pull-to-refresh.
- **RecyclerView (Grid)** — 2-column grid displaying image cards. Each card shows the photo thumbnail and the author name below it.
- **ProgressBar (initial load)** — Centered full-screen spinner shown only on the very first load.
- **ProgressBar (pagination)** — Small spinner at the bottom of the list while loading the next page.
- **Error View** — A centered message + Retry button shown when the API fails and no cache exists.
- **Offline Banner** — A subtle top banner saying "You are offline — showing cached content" when there is no network.

---

## Screen 2: Image Details Screen (`ImageDetailsActivity`)

**File:** `ImageDetailsActivity.kt` + `activity_image_details.xml`

### Components

- **Toolbar** — Back arrow + image ID as title + Favorite toggle button (heart icon, filled/unfilled)
- **Full-size ImageView** — Displays the full image using Glide, with a loading spinner while it loads.
- **Author name** — `TextView` below the image
- **Dimensions** — `TextView` showing width × height
- **Download URL** — `TextView` (copyable on long press)
- **Favorites Strip** — Same persistent favorites strip as the Main Screen (always visible)

---

## Screen 3: Favorites Screen (`FavoritesActivity`) _(accessible from any screen via toolbar icon)_

**File:** `FavoritesActivity.kt` + `activity_favorites.xml`

### Components

- **Toolbar** — "Favorites" title + back button
- **RecyclerView (Grid)** — 2-column grid of up to 5 favorite images
- **Empty State View** — Message "No favorites yet. Tap ❤ on any image to add." shown when list is empty
- **Favorites Strip** — Persistent strip (same as other screens)
